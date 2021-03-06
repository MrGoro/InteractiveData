package com.github.mrgoro.interactivedata.api.service;

import com.github.mrgoro.interactivedata.api.chart.annotations.Chart;
import com.github.mrgoro.interactivedata.api.chart.data.ChartData;
import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.ChartPostProcessor;
import com.github.mrgoro.interactivedata.api.chart.processors.AnnotationProcessHelper;
import com.github.mrgoro.interactivedata.api.chart.processors.AnnotationProcessor;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;
import com.github.mrgoro.interactivedata.api.util.ReflectionUtil;
import com.github.mrgoro.interactivedata.api.util.exceptions.ChartDefinitionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Service for providing definitions of all charts.
 *
 * Information is loaded during initialization of the service. ServiceProvider is used to find Classes to further search for.
 * For performance reasons the initialization should take place during applications startup.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class ChartDefinitionService {

    private static final Log log = LogFactory.getLog(ChartDefinitionService.class);

    private ServiceProvider serviceProvider;

    private Map<String, AbstractChartDefinition<?, ? extends ChartData>> chartDefinitions = new HashMap<>();

    /**
     * Create a new ChartDefinitionService using a service provider.
     *
     * The service provider is used to provide classes that should be further searched for appropriate annotations.
     *
     * @param serviceProvider Service provider
     */
    public ChartDefinitionService(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        loadChartDefinitionsUsingAnnotations();
    }

    /**
     * Get a Map with all {@link AbstractChartDefinition ChartDefinitions}.
     *
     * The name of the chart is the key of the Map.
     *
     * @return Map with all {@link AbstractChartDefinition ChartDefinitions}
     */
    public Map<String, AbstractChartDefinition<?, ? extends ChartData>> getChartDefinitions() {
        return chartDefinitions;
    }

    /**
     * Get a {@link AbstractChartDefinition ChartDefinition} for the chart with the given name.
     * @param name Name of the chart
     * @return {@link AbstractChartDefinition ChartDefinition}
     */
    public Optional<AbstractChartDefinition<?, ?>> getChartDefinition(String name) {
        return Optional.ofNullable(chartDefinitions.get(name));
    }

    /**
     * Create {@link AbstractChartDefinition ChartDefinitions} using the annotations.
     */
    public void loadChartDefinitionsUsingAnnotations() {
        log.debug("Loading ChartDefinitions using annotations");
        Collection<Object> chartServices = serviceProvider.getChartServices();
        for(Object chartService : chartServices) {
            log.debug("Processing ChartService [" + chartService.getClass().getSimpleName() + "]");
            ChartService serviceAnnotation = chartService.getClass().getAnnotation(ChartService.class);
            List<Method> methods = ReflectionUtil.findAnnotatedMethods(chartService.getClass(), Chart.class);
            methods.forEach(method -> processMethodAnnotations(chartService, method, serviceAnnotation));
        }
    }

    /**
     * Extract the {@link AbstractChartDefinition ChartDefinition} from a Method.
     *
     * Uses an {@link AnnotationProcessor AnnotationProcessor}
     * suitable for the given annotation to extract the information.
     */
    @SuppressWarnings("unchecked")
    private void processMethodAnnotations(Object bean, Method method, ChartService serviceAnnotation) {
        log.debug("Processing chart annotations on method: " + method.getName());
        Annotation[] annotations = method.getDeclaredAnnotations();

        Chart chart = null;
        Annotation chartAnnotation = null;
        for(Annotation annotation : annotations) {
            if(annotation.annotationType() == Chart.class) {
                chart = (Chart) annotation;
            } else {
                chartAnnotation = annotation;
            }
        }

        ChartPostProcessor chartPostProcessor = ChartPostProcessor.identity();
        // Method has one parameter of type ? extends ChartData
        if(method.getParameterCount() == 1 && ChartData.class.isAssignableFrom(method.getParameterTypes()[0])) {
            Class<? extends ChartData> dataType = (Class<? extends ChartData>) method.getReturnType();
            chartPostProcessor = data -> {
                try {
                    return dataType.cast(method.invoke(bean, data));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    log.error("Cannot access Method annotated with @Chart for post processing, " + e.getMessage());
                    return data;
                }
            };
        } else {
            log.info("Method with @Chart annotation does not have ChartData or any sub class as its parameter type. Skipping post processing.");
        }

        if(chart != null && chartAnnotation != null) {
            String chartTypeName = chartAnnotation.annotationType().getSimpleName();
            try {
                AbstractChartDefinition<?, ?> chartDefinition = serviceProvider
                        .getAnnotationProcessor(chartAnnotation)
                        .orElseThrow(() -> new ChartDefinitionException("No AnnotationProcessor found for chart annotation [" + chartTypeName + "]"))
                        .process(chartAnnotation);
                chartDefinition.setChartPostProcessor(chartPostProcessor);
                AnnotationProcessHelper.processChartAnnotation(chartDefinition, chart, serviceAnnotation);
                chartDefinitions.put(chartDefinition.getServiceName(), chartDefinition);
                log.info("Chart of type [" + chartTypeName + "] with id [" + chartDefinition.getName() + "]");
            } catch (IllegalArgumentException | ChartDefinitionException e) {
                log.warn("Error Processing Annotation [" + chartAnnotation.annotationType().getSimpleName() + "]", e);
            }
        } else {
            log.warn("Invalid Chart definition on Method " + method.getName());
        }
    }
}
