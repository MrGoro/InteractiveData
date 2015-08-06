package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import de.schuermann.interactivedata.spring.data.processors.FilterProcessor;
import de.schuermann.interactivedata.spring.rest.AbstractApiBuilder;
import de.schuermann.interactivedata.spring.rest.ApiBuilder;
import de.schuermann.interactivedata.spring.util.AdvancedReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

/**
 * Service for searching the correct implementations of interfaces.
 *
 * @author Philipp Schürmann
 */
@Service
public class ProcessorService {

    private String path;
    private ApplicationContext applicationContext;

    @Autowired
    public ProcessorService(InteractiveDataProperties properties, ApplicationContext applicationContext) {
        this.path = properties.getPath();
        this.applicationContext = applicationContext;
    }

    @Deprecated
    public Class<? extends AbstractApiBuilder> findApiBuilder(Class<? extends AbstractChartDefinition> chartDefinition) {
        return (Class<? extends AbstractApiBuilder>) AdvancedReflectionUtil.getGenericImplementation(ApiBuilder.class, chartDefinition, path);
    }

    /**
     * Returns a FilterProcessor Bean that can handle the Filter specified.
     *
     * @param filterClass Class the Filter can handle
     * @param <P> Filter
     * @return FilterProcessor
     */
    public <P extends Filter> FilterProcessor getFilterProcessor(Class<P> filterClass) {
        return applicationContext.getBean(findFilterProcessor(filterClass));
    }

    /**
     * Returns a Class that implements the Interface <code>FilterProcessor<FilterType></code>
     *
     * @param filterType Type of the Filter
     * @param <D> Type of the Filter
     * @return Class implementing FilterProcessor<FilterType>
     */
    private <D> Class<? extends FilterProcessor> findFilterProcessor(Class<D> filterType) {
        return AdvancedReflectionUtil.getGenericImplementation(FilterProcessor.class, filterType, this.path);
    }

    /**
     * Get an AnnotationProcessor that is capable of processing the given Annotation.
     *
     * @param annotation Annotation for processing
     * @return AnnotationProcessor
     * @throws IllegalArgumentException when no {@Link AnnotationProcessor} was found or could be instantiated for the given annotation
     */
    public AnnotationProcessor getAnnotationProcessor(Annotation annotation) {
        Class<? extends AnnotationProcessor> processorClass = findAnnotationProcessor(annotation);
        try {
            return AdvancedReflectionUtil.getInstance(processorClass);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot get AnnotationProcessor for Annotation [" + annotation.getClass().getName() + "]", e);
        }
    }


    /**
     * Returns a Class that implements the Interface AnnotationProcessor<AnnotationType>
     *
     * @param annotation Type of the annotation the processor shuld handle
     * @return Class implementing AnnotationProcessor<AnnotationType>
     */
    private Class<? extends AnnotationProcessor> findAnnotationProcessor(Annotation annotation) {
        return AdvancedReflectionUtil.getGenericImplementation(AnnotationProcessor.class, annotation.annotationType(), this.path);
    }

}
