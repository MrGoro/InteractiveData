package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.chart.annotations.Chart;
import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessHelper;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.api.util.ReflectionUtil;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Philipp Schürmann
 */
public class ChartDefinitionService {

    private Log log = LogFactory.getLog(ChartDefinitionService.class);

    private ServiceProvider serviceProvider;

    private Map<String, AbstractChartDefinition<?, ? extends ChartData>> chartDefinitions = new HashMap<>();

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
    public Map<String, AbstractChartDefinition<?, ? extends ChartData>> getChartDefinitions() {
        return chartDefinitions;
    }

    /**
     * Get a {@Link AbstractChartDefinition ChartDefinition} for the chart with the given name.
     * @param name Name of the chart
     * @return {@Link AbstractChartDefinition ChartDefinition}
     */
    public Optional<AbstractChartDefinition<?, ?>> getChartDefinition(String name) {
        return Optional.ofNullable(chartDefinitions.get(name));
    }

    /**
     * Search for {@Link AbstractChartDefinition ChartDefinitions} using the Annotations.
     */
    private void loadChartDefinitionsUsingAnnotations() {
        Collection<Object> chartServices = serviceProvider.getChartServices();
        for(Object chartService : chartServices) {
            ChartService serviceAnnotation = chartService.getClass().getAnnotation(ChartService.class);
            List<Method> methods = ReflectionUtil.findAnnotatedMethods(chartService.getClass(), Chart.class);
            methods.forEach(method -> processMethodAnnotations(chartService, method, serviceAnnotation));
        }
    }

    /**
     * Extract the {@Link AbstractChartDefinition ChartDefinition} from a Method.
     *
     * Uses an {@Link AnnotationProcessor AnnotationProcessor} suitable for the given annotation to extract the
     * information.
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

        if(chart != null && chartAnnotation != null) {
            String chartTypeName = chartAnnotation.annotationType().getSimpleName();
            try {
                AbstractChartDefinition<?, ?> chartDefinition = serviceProvider
                        .getAnnotationProcessor(chartAnnotation)
                        .get()
                        .process(chartAnnotation);
                chartDefinition.setChartPostProcessor(chartPostProcessor);
                AnnotationProcessHelper.processChartAnnotation(chartDefinition, chart, serviceAnnotation);
                chartDefinitions.put(chartDefinition.getName(), chartDefinition);
                log.info("Chart of type [" + chartTypeName + "] with id [" + chartDefinition.getName() + "]");
            } catch (NoSuchElementException | IllegalArgumentException e) {
                log.warn("Error Processing Annotation [" + chartAnnotation.annotationType().getSimpleName() + "]", e);
            }
        } else {
            log.warn("Invalid Chart definition on Method " + method.getName());
        }
    }

}
