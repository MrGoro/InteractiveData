package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.annotations.Chart;
import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.util.ReflectionUtil;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import de.schuermann.interactivedata.spring.service.locators.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Philipp Schürmann
 */
@Service
public class ChartDefinitionService {

    private Log log = LogFactory.getLog(ChartDefinitionService.class);

    private ServiceProvider serviceProvider;

    private Map<String, AbstractChartDefinition> chartDefinitions = new HashMap<>();

    @Autowired
    public ChartDefinitionService(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        loadChartDefinitionsUsingAnnotations();
    }

    /**
     * Get a Map with all {@Link AbstractChartDefinition ChartDefinitions}.
     *
     * The name of the chart is the key of the Map.
     *
     * @return Map with all {@Link AbstractChartDefinition ChartDefinitions}
     */
    public Map<String, AbstractChartDefinition> getChartDefinitions() {
        return chartDefinitions;
    }

    /**
     * Get a {@Link AbstractChartDefinition ChartDefinition} for the chart with the given name.
     * @param name Name of the chart
     * @return {@Link AbstractChartDefinition ChartDefinition}
     */
    public AbstractChartDefinition getChartDefinition(String name) {
        return chartDefinitions.get(name);
    }

    /**
     * Search for {@Link AbstractChartDefinition ChartDefinitions} using the Annotations.
     */
    private void loadChartDefinitionsUsingAnnotations() {
        Collection<Object> chartServices = serviceProvider.getChartServices();
        for(Object chartService : chartServices) {
            List<Method> methods = ReflectionUtil.findAnnotatedMethods(chartService.getClass(), Chart.class);
            methods.forEach(method -> processMethodAnnotations(chartService, method));
        }
    }

    /**
     * Extract the {@Link AbstractChartDefinition ChartDefinition} from a Method.
     *
     * Uses an {@Link AnnotationProcessor AnnotationProcessor} suitable for the given annotation to extract the
     * information.
     */
    private void processMethodAnnotations(Object bean, Method method) {
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
            try {
                AbstractChartDefinition chartDefinition = serviceProvider
                        .getAnnotationProcessor(chartAnnotation)
                        .get()
                        .process(name, chartAnnotation, chartPostProcessor);
                chartDefinitions.put(chartDefinition.getName(), chartDefinition);
            } catch (NoSuchElementException | IllegalArgumentException e) {
                log.warn(e.getMessage());
            }
        } else {
            log.warn("Invalid Chart definition on Method " + method.getName());
        }
    }

}
