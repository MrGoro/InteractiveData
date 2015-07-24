package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.ChartApi;
import de.schuermann.interactivedata.api.chart.annotations.Chart;
import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import de.schuermann.interactivedata.spring.data.processors.FilterProcessor;
import de.schuermann.interactivedata.spring.util.ReflectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
@Service
public class ReflectionService {

    private Log log = LogFactory.getLog(ReflectionService.class);

    private String path;
    private ApplicationContext applicationContext;

    @Autowired
    public ReflectionService(InteractiveDataProperties properties, ApplicationContext applicationContext) {
        this.path = properties.getPath();
        this.applicationContext = applicationContext;
    }

    public List<AbstractChartDefinition> getChartDefinitions() {
        List<AbstractChartDefinition> chartDefinitions = new ArrayList<>();

        List<Class<?>> apiClasses = ReflectionUtil.findAnnotatedClasses(this.path, ChartApi.class);
        for(Class<?> apiClass : apiClasses) {
            Object bean = null;
            try {
                bean = applicationContext.getBean(apiClass);
            } catch(NoSuchBeanDefinitionException e) {
                log.debug("Class [" + apiClass.getName() + "] with @Chart annotations is no bean. Methods have to be static.");
            }
            List<Method> methods = ReflectionUtil.findAnnotatedMethods(apiClass, Chart.class);
            final Object finalBean = bean;
            methods.forEach(method -> chartDefinitions.add(processMethodAnnotations(finalBean, method)));
        }

        return chartDefinitions;
    }

    /**
     * Extract the {@Link AbstractChartDefinition ChartDefinition} from a Method.
     *
     * Uses an {@Link AnnotationProcessor AnnotationProcessor} suitable for the given annotation to extract the
     * information.
     *
     *
     * @param bean
     * @param method Method that has the appropriate annotations {@Link Chart} and the specific api annotation.
     * @return Definition of the chart
     */
    @SuppressWarnings("unchecked")
    private AbstractChartDefinition processMethodAnnotations(Object bean, Method method) {
        log.debug("Generating API for Chart: " + method.getName());
        Annotation[] annotations = method.getDeclaredAnnotations();

        String name = null;
        Annotation chartAnnotation = null;
        for(Annotation annotation : annotations) {
            if(annotation.annotationType() == Chart.class) {
                Chart chart = (Chart) annotation;
                name = chart.value();
            } else {
                chartAnnotation = annotation;
            }
        }

        long appropriateTypesCount = Arrays.asList(method.getParameterTypes())
                .stream()
                .filter(ChartData.class::isAssignableFrom)
                .count();

        ChartPostProcessor chartPostProcessor = data -> data;
        if(method.getParameterCount() == appropriateTypesCount) {
            if(ChartData.class.isAssignableFrom(method.getReturnType())) {
                final Class<? extends ChartData> dataType = (Class<? extends ChartData>) method.getReturnType();
                chartPostProcessor = data -> {
                    try {
                        return dataType.cast(method.invoke(bean, data));
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        log.error("Cannot access Method annotated with @Chart for post processing, " + e.getMessage());
                        return data;
                    }
                };
            } else {
                log.warn("Method with @Chart annotation does not have ChartData or any sub class as its return type. Return Type is required if the method should post process auto generated ChartData. Skipping post process.");
            }
        } else {
            log.info("Method with @Chart annotation does not have ChartData or any sub class as its parameter type. Skipping post processing.");
        }

        if(name != null && chartAnnotation != null) {
            log.info("Processing Detail-Configuration for Chart: " + name);

            Class<? extends AnnotationProcessor> processorClass = ReflectionUtil.getAnnotationProcessor(chartAnnotation, path);
            if(processorClass != null) {
                try {
                    Constructor constructor = processorClass.getConstructor();
                    AnnotationProcessor annotationProcessor = (AnnotationProcessor) constructor.newInstance();
                    AbstractChartDefinition chartDefinition = annotationProcessor.process(name, chartAnnotation);
                    chartDefinition.setChartPostProcessor(chartPostProcessor);
                    return chartDefinition;
                } catch (NoSuchMethodException e) {
                    log.error("Processor [" + processorClass.getName() + " for Chart [" + name + "] does not have an empty constructor");
                } catch (InvocationTargetException e) {
                    log.error("Constructor of Processor [" + processorClass.getName() + " for Chart [" + name + "] cannot be invoked: " + e.getMessage());
                } catch (InstantiationException e) {
                    log.error("Constructor of Processor [" + processorClass.getName() + " for Chart [" + name + "] cannot be instantiated: " + e.getMessage());
                } catch (IllegalAccessException e) {
                    log.error("Constructor of Processor [" + processorClass.getName() + " for Chart [" + name + "] has different arguments: " + e.getMessage());
                }
            } else {
                log.error("Could not find Processor for Annotation " + chartAnnotation.annotationType().getName() + " - Chart not generated!");
            }
        } else {
            log.warn("Invalid Chart definition on Method " + method.getName());
        }
        return null;
    }

    public <D> FilterProcessor getFilterProcessor(Class<D> filter) {
        Class<? extends FilterProcessor> prozessorClazz = ReflectionUtil.getGenericImplementation(FilterProcessor.class, filter, path);
        return applicationContext.getBean(prozessorClazz);
    }

}
