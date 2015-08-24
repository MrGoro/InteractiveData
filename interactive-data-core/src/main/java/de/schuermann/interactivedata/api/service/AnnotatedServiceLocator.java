package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.handler.ChartRequestHandler;
import de.schuermann.interactivedata.api.service.annotations.AnnotationProcessorService;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.api.util.AnnotationToLongFunction;
import de.schuermann.interactivedata.api.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * Default Implementation of an {@link ServiceLocator} that uses the Pluggable Annotation Processing API
 * (JSR-269) to find Services that are annotated with Service Annotations.
 *
 * @author Philipp Sch&uuml;rmann
 */
public abstract class AnnotatedServiceLocator implements ServiceLocator {

    @Override
    public Collection<Class<?>> getChartServices() {
        return getServices(ChartService.class);
    }

    @Override
    public Collection<Class<?>> getAnnotationProcessorServices() {
        return getServices(AnnotationProcessorService.class);
    }

    @Override
    public Collection<Class<?>> getChartRequestHandlerServices() {
        return getServices(ChartRequestHandlerService.class);
    }

    private AnnotationToLongFunction<AnnotationProcessorService> annotationProcessorServicePriorityFunction =
            new AnnotationToLongFunction<>(AnnotationProcessorService.class, "value");

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Class<? extends AnnotationProcessor<A>> getAnnotationProcessorService(Class<A> annotationClass) {
        // Get all suitable Services
        Collection<Class<?>> serviceClasses = getAnnotationProcessorServices();

        // Filter by generic implementation and get the one with the lowest priority value
        Optional<Class<?>> processorClass = serviceClasses.stream()
                .filter(aClass -> ReflectionUtil.isGenericImplementation(aClass, AnnotationProcessor.class, annotationClass))
                .sorted(Comparator.comparingLong(annotationProcessorServicePriorityFunction))
                .findFirst();

        // Make it typed (already checked in filter)
        Class<? extends AnnotationProcessor<A>> processorClassTyped = null;
        if(processorClass.isPresent()) {
            processorClassTyped = (Class<? extends AnnotationProcessor<A>>) processorClass.get();
        }

        return processorClassTyped;
    }

    private AnnotationToLongFunction<ChartRequestHandlerService> chartReqeustHandlerServicePriorityFunction =
            new AnnotationToLongFunction<>(ChartRequestHandlerService.class, "value");

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractChartDefinition<?, ?>> Class<? extends ChartRequestHandler<T, ?>> getChartRequestHandlerService(Class<? extends AbstractChartDefinition> chartDefinition) {
        // Get all suitable Services
        Collection<Class<?>> serviceClasses = getChartRequestHandlerServices();

        // Filter by generic implementation and get the one with the lowest priority value
        Optional<Class<?>> requestHandlerClasses = serviceClasses.stream()
                .filter(aClass -> ReflectionUtil.isGenericImplementation(aClass, ChartRequestHandler.class, chartDefinition))
                .sorted(Comparator.comparingLong(chartReqeustHandlerServicePriorityFunction))
                .findFirst();

        // Make it typed (already checked in filter)
        Class<? extends ChartRequestHandler<T, ?>> requestHandlerClassesTyped = null;
        if(requestHandlerClasses.isPresent()) {
            requestHandlerClassesTyped = (Class<? extends ChartRequestHandler<T, ?>>) requestHandlerClasses.get();
        }

        return requestHandlerClassesTyped;
    }
}
