package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.handler.ChartRequestHandler;
import de.schuermann.interactivedata.api.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * @author Philipp Schürmann
 */
public abstract class ServiceProvider {

    private ServiceLocator serviceLocator;

    public ServiceProvider(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    /**
     * Provides an instance of a given class.
     * Returns null if Class is null.
     *
     * @param tClass Class to build the instance from.
     * @param <T> Type of the Instance
     * @return Instance of Type T
     */
    protected <T> T getInstanceOfClass(Class<T> tClass) {
        if(tClass == null) {
            return null;
        }
        return ReflectionUtil.getInstance(tClass);
    }

    /**
     * Get all Chart {@Link ChartService ChartServices} as Beans
     *
     * @return Collection of all {@Link ChartService ChartServices} as Beans.
     */
    public Collection<Object> getChartServices() {
        Collection<Class<?>> chartServiceClasses = serviceLocator.getChartServices();
        Collection<Object> beans =  chartServiceClasses.stream()
                .map(chartServiceClass -> (Object) getInstanceOfClass(chartServiceClass))
                .filter(Objects::nonNull)
                .collect(toList());
        return beans;
    }

    /**
     * Get an AnnotationProcessor that is capable of processing the given Annotation.
     *
     * @param annotation Annotation
     * @return AnnotationProcessor
     * @throws IllegalArgumentException when no {@Link AnnotationProcessor} was found or could be instantiated for the given annotation
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Optional<AnnotationProcessor<A>> getAnnotationProcessor(A annotation) {
        Class<A> annotationClass = (Class<A>) annotation.annotationType();
        Class<? extends AnnotationProcessor<A>> processorClass = serviceLocator.getAnnotationProcessorService(annotationClass);
        AnnotationProcessor<A> processor = getInstanceOfClass(processorClass);
        return Optional.ofNullable(processor);
    }

    /**
     * Get an {@Link de.schuermann.interactivedata.api.handler.ChartRequestHandler} that is capable of processing the given
     * {@Link de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition ChartDefinition}.
     *
     * @param chartDefinition ChartDefinition to process with the ChartRequestHandler
     * @param <T> Type of the ChartDefinition
     * @return ChartRequest handle capable of handling requests for charts with the given ChartDefinition
     */
    public <T extends AbstractChartDefinition<?, ? extends ChartData>> Optional<ChartRequestHandler<T, ?>> getChartRequestHandler(T chartDefinition) {
        Class<? extends ChartRequestHandler<T, ?>> requestHandlerClass = serviceLocator.getChartRequestHandlerService(chartDefinition.getClass());
        ChartRequestHandler<T, ?> requestHandler = getInstanceOfClass(requestHandlerClass);
        return Optional.ofNullable(requestHandler);
    }

    public DataSource getDataSource(Class<? extends DataSource> dataSourceClass) {
        return getInstanceOfClass(dataSourceClass);
    }

    protected ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}
