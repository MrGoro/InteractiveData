package de.schuermann.interactivedata.spring.config;

import de.schuermann.interactivedata.api.ChartApi;
import de.schuermann.interactivedata.api.chart.types.Chart;
import de.schuermann.interactivedata.api.chart.types.line.LineChart;
import de.schuermann.interactivedata.spring.rest.AnnotationProcessor;
import de.schuermann.interactivedata.spring.rest.LineChartProcessor;
import de.schuermann.interactivedata.spring.util.ReflectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
@Component
@ApplicationPath("interactivedata")
public class ApiConfig extends ResourceConfig {

    private Log log = LogFactory.getLog(ResourceConfig.class);

    private String path;

    @Autowired
    public ApiConfig(ServletContext servletContext, InteractiveDataProperties properties) {
        this.path = properties.getPath();

        List<Resource> resources = new ArrayList<>();

        List<Class<?>> apiClasses = ReflectionUtil.findAnnotatedClasses(this.path, ChartApi.class);
        for(Class<?> apiClass : apiClasses) {
            List<Method> methods = ReflectionUtil.findAnnotatedMethods(apiClass, Chart.class);
            methods.forEach(method -> resources.add(processMethodAnnotations(method)));
        }

        WebApplicationContext springFactory = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        resources.forEach(this::registerResources);
    }

    private Resource processMethodAnnotations(Method method) {
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
        if(name != null && chartAnnotation != null) {
            log.info("Processing Detail-Configuration for Chart: " + name);

            // Find Processor for Annotation
            List<Class<?>> processorClasses = ReflectionUtil.findAssignableClasses(this.path, AnnotationProcessor.class);
            for (Class<?> processorClass : processorClasses) {
                Type[] interfaceTypes = processorClass.getGenericInterfaces();
                for (Type interfaceType : interfaceTypes) {
                    ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
                    for(Type genericType : parameterizedType.getActualTypeArguments()) {
                        if (genericType == chartAnnotation.annotationType()) {
                            try {
                                Constructor constructor = processorClass.getConstructor();
                                AnnotationProcessor annotationProcessor = (AnnotationProcessor) constructor.newInstance();
                                return annotationProcessor.process(name, chartAnnotation);
                            } catch (NoSuchMethodException e) {
                                log.error("Processor [" + processorClass.getName() + " for Chart [" + name + "] does not have an empty constructor");
                            } catch (InvocationTargetException e) {
                                log.error("Constructor of Processor [" + processorClass.getName() + " for Chart [" + name + "] cannot be invoked: " + e.getMessage());
                            } catch (InstantiationException e) {
                                log.error("Constructor of Processor [" + processorClass.getName() + " for Chart [" + name + "] cannot be instantiated: " + e.getMessage());
                            } catch (IllegalAccessException e) {
                                log.error("Constructor of Processor [" + processorClass.getName() + " for Chart [" + name + "] has different arguments: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            LineChartProcessor processor = new LineChartProcessor();
            processor.process(name, (LineChart) chartAnnotation);
        } else {
            log.warn("@Chart has no value on Method " + method.getName());
        }
        return null;
    }
}
