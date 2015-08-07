package de.schuermann.interactivedata.api.service.annotations;

import de.schuermann.interactivedata.processors.ProcessedAnnotation;

import java.lang.annotation.*;

/**
 * Annotation to mark a {@Link de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor} as a
 * Services. This makes the service available.
 *
 * Custom AnnotationProcessors that extend the {@Link de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor}
 * are already marked as AnnotationProcessorService as the Annotation inherits.
 *
 * @author Philipp Schürmann
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ProcessedAnnotation
public @interface AnnotationProcessorService {

    /**
     * Define the priority of the service.
     *
     * If multiple services are suitable for the same situation,
     * the one with the lower priority value will be chosen.
     *
     * @return Priority of the service as a long value
     */
    long value() default 1000;
}
