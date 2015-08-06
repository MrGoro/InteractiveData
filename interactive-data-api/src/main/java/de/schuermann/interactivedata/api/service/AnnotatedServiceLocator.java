package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;
import de.schuermann.interactivedata.api.service.annotations.AnnotationProcessorService;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.api.service.annotations.FilterService;
import de.schuermann.interactivedata.api.util.AnnotationToLongFunction;
import de.schuermann.interactivedata.api.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * Default Implementation of an {@Link ServiceLocator} that uses the Pluggable Annotationn Processing API
 * (JSR-269) to find Services that are annotated with Service Annotations.
 *
 * @author Philipp Schürmann
 */
public abstract class AnnotatedServiceLocator implements ServiceLocator {

    @Override
    public Collection<Class<?>> getChartServices() {
        return getServices(ChartService.class);
    }

    @Override
    public Collection<Class<?>> getFilterServices() {
        return getServices(FilterService.class);
    }

    @Override
    public Collection<Class<?>> getAnnotationProcessorServices() {
        return getServices(AnnotationProcessorService.class);
    }

    private AnnotationToLongFunction<FilterService> filterServicePriorityFunction =
            new AnnotationToLongFunction<>(FilterService.class, "value");

    @Override
    @SuppressWarnings("unchecked")
    public <D extends FilterData> Optional<Class<? extends Filter<D>>> getFilterService(Class<D> dataClass) {
        // Get all suitable Services
        Collection<Class<?>> serviceClasses = getFilterServices();

        // Filter by generic implementation and get the one with the lowest priority value
        Optional<Class<?>> filterClass = serviceClasses.stream()
                .filter(aClass -> ReflectionUtil.isGenericImplementation(aClass, Filter.class, dataClass))
                .sorted(Comparator.comparingLong(filterServicePriorityFunction))
                .findFirst();

        // Make it typed (already checked in filter)
        Class<? extends Filter<D>> filterClassTyped = null;
        if(filterClass.isPresent()) {
            filterClassTyped = (Class<? extends Filter<D>>) filterClass.get();
        }

        return Optional.ofNullable(filterClassTyped);
    }

    private AnnotationToLongFunction<AnnotationProcessorService> annotationProcessorServicePriorityFunction =
            new AnnotationToLongFunction<>(AnnotationProcessorService.class, "value");

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Optional<Class<? extends AnnotationProcessor<A>>> getAnnotationProcessorService(Class<A> annotationClass) {
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

        return Optional.ofNullable(processorClassTyped);
    }
}
