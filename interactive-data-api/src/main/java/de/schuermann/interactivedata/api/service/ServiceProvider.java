package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.data.DataSource;
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
     *
     * @param tClass Class to build the instance from.
     * @param <T> Type of the Instance
     * @return Instance of Type T
     */
    protected <T> T getInstanceOfClass(Class<T> tClass) {
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
        Optional<Class<? extends AnnotationProcessor<A>>> processorClass = serviceLocator.getAnnotationProcessorService(annotationClass);
        AnnotationProcessor<A> processor = null;
        try {
            if(processorClass.isPresent()) {
                processor = getInstanceOfClass(processorClass.get());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot get AnnotationProcessor for Annotation [" + annotationClass.getName() + "]", e);
        }
        return Optional.ofNullable(processor);
    }

    public abstract DataSource getDataSource(Class<? extends DataSource> dataSourceClass);

    protected ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}
