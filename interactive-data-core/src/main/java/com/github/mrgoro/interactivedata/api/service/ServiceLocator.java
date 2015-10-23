package com.github.mrgoro.interactivedata.api.service;

import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.processors.AnnotationProcessor;
import com.github.mrgoro.interactivedata.api.handler.ChartRequestHandler;
import com.github.mrgoro.interactivedata.api.service.annotations.AnnotationProcessorService;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartRequestHandlerService;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Philipp Sch&uuml;rmann
 */
public interface ServiceLocator {

    /**
     * Get all services registered with the specified Annotation
     *
     * @param annotation Annotation to search for
     * @return Collection of Classes that are registered Services.
     */
    Collection<Class<?>> getServices(Class<? extends Annotation> annotation);

    /**
     * Get all registered {@link ChartService ChartServices}.
     *
     * @return Collection of Classes that are registered as {@link ChartService ChartService}
     */
    Collection<Class<?>> getChartServices();

    /**
     * Get all registered {@link AnnotationProcessorService AnnotationProcessorServices}.
     *
     * @return Collection of Classes that are registered as {@link AnnotationProcessorService AnnotationProcessorService}
     */
    Collection<Class<?>> getAnnotationProcessorServices();

    /**
     * Get all registered {@link AnnotationProcessorService AnnotationProcessorServices}.
     *
     * @return Collection of Classes that are registered as {@link AnnotationProcessorService AnnotationProcessorService}
     */
    Collection<Class<?>> getChartRequestHandlerServices();

    /**
     * Get the Class of the {@link AnnotationProcessor} that
     * can use the specified Annotation.
     *
     * This will respect the priority of the {@link AnnotationProcessorService AnnotationProcessorService}.
     *
     * @param annotationClass Class of the Annotation that should be processed
     * @param <A> Annotation Type
     * @return Class of the {@link AnnotationProcessor}
     */
    <A extends Annotation> Class<? extends AnnotationProcessor<A>> getAnnotationProcessorService(Class<A> annotationClass);

    /**
     * Get the Class of the {@link ChartRequestHandler} that is capable of
     * handling Request for charts of the given {@link AbstractChartDefinition ChartDefinition}.
     *
     * This will respect the priority of the {@link ChartRequestHandlerService ChartRequestHandlerService}.
     *
     * @param chartDefinition Class of the ChartDefinition
     * @param <T> Type of the ChartDefinition
     * @return Class of the {@link ChartRequestHandler}
     */
    <T extends AbstractChartDefinition<?, ?>> Class<? extends ChartRequestHandler<T, ?>> getChartRequestHandlerService(Class<? extends AbstractChartDefinition> chartDefinition);
}
