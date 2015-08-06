package de.schuermann.interactivedata.spring.service.locators;

import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.service.ServiceLocator;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import de.schuermann.interactivedata.spring.data.processors.FilterProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Service for searching the correct implementations of interfaces.
 *
 * @author Philipp Schürmann
 */
@Service
public class ServiceProvider {

    private InteractiveDataProperties properties;
    private ApplicationContext applicationContext;
    private ServiceLocator serviceLocator;

    @Autowired
    public ServiceProvider(InteractiveDataProperties properties, ApplicationContext applicationContext, ServiceLocator serviceLocator) {
        this.properties = properties;
        this.applicationContext = applicationContext;
        this.serviceLocator = serviceLocator;
    }

    /**
     * Returns a FilterProcessor Bean that can handle the Filter specified.
     *
     * @param filterClass Class the Filter can handle
     * @param <P> Filter
     * @return FilterProcessor
     */
    @Deprecated
    public <P extends Filter> FilterProcessor getFilterProcessor(Class<P> filterClass) {
        return applicationContext.getBean(
                ClassLocatorUtil.getGenericImplementation(FilterProcessor.class, filterClass, properties.getPath())
        );
    }

    /**
     * Get all Chart {@Link ChartService ChartServices} as Beans
     *
     * @return Collection of all {@Link ChartService ChartServices} as Beans.
     */
    public Collection<Object> getChartServices() {
        Collection<Class<?>> chartServiceClasses = serviceLocator.getChartServices();
        Collection<Object> beans =  chartServiceClasses.stream()
                .map(chartServiceClass -> {
                    try {
                        return (Object) applicationContext.getBean(chartServiceClass);
                    } catch (BeansException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toList());
        return beans;
    }

    /**
     * Get an AnnotationProcessor that is capable of processing the given Annotation as a Bean.
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
                processor = applicationContext.getBean(processorClass.get());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot get AnnotationProcessor for Annotation [" + annotationClass.getName() + "]", e);
        }
        return Optional.ofNullable(processor);
    }

}
