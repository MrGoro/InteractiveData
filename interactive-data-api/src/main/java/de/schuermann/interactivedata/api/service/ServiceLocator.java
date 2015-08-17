package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.handler.ChartRequestHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Philipp Schürmann
 */
public interface ServiceLocator {

    /**
     * Get all services registered with the specified Annotation
     *
     * @return Collection of Classes that are registered Services.
     */
    Collection<Class<?>> getServices(Class<? extends Annotation> annotation);

    /**
     * Get all registered {@Link ChartService ChartServices}.
     *
     * @return Collection of Classes that are registered as {@Link ChartService ChartService}
     */
    Collection<Class<?>> getChartServices();

    /**
     * Get all registered {@Link AnnotationProcessorService AnnotationProcessorServices}.
     *
     * @return Collection of Classes that are registered as {@Link AnnotationProcessorService AnnotationProcessorService}
     */
    Collection<Class<?>> getAnnotationProcessorServices();

    /**
     * Get all registered {@Link AnnotationProcessorService AnnotationProcessorServices}.
     *
     * @return Collection of Classes that are registered as {@Link AnnotationProcessorService AnnotationProcessorService}
     */
    Collection<Class<?>> getChartRequestHandlerServices();

    /**
     * Get the Class of the {@Link de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor} that
     * can use the specified Annotation.
     *
     * This will respect the priority of the {@Link AnnotationProcessorService AnnotationProcessorService}.
     *
     * @param annotationClass Class of the Annotation that should be processed
     * @param <A> Annotation Type
     * @return Class of the {@Link de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor}
     */
    <A extends Annotation> Class<? extends AnnotationProcessor<A>> getAnnotationProcessorService(Class<A> annotationClass);

    /**
     * Get the Class of the {@Link de.schuermann.interactivedata.api.handler.ChartRequestHandler} that is capable of
     * handling Request for charts of the given {@Link de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition ChartDefinition}.
     *
     * This will respect the priority of the {@Link ChartRequestHandlerService ChartRequestHandlerService}.
     *
     * @param chartDefinition Class of the ChartDefinition
     * @param <T> Type of the ChartDefinition
     * //@param <D> Type of the ChartData
     * @return Class of the {@Link de.schuermann.interactivedata.api.handler.ChartRequestHandler}
     */
    <T extends AbstractChartDefinition<?, ?>> Class<? extends ChartRequestHandler<T, ?>> getChartRequestHandlerService(Class<? extends AbstractChartDefinition> chartDefinition);
}
