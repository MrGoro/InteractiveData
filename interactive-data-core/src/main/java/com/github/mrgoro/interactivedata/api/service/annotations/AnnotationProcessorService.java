package com.github.mrgoro.interactivedata.api.service.annotations;

import com.github.mrgoro.interactivedata.api.chart.processors.AnnotationProcessor;
import com.github.mrgoro.interactivedata.processors.ProcessedAnnotation;

import java.lang.annotation.*;

/**
 * Annotation to mark a {@link AnnotationProcessor} as a
 * Services. This makes the service available.
 *
 * Custom AnnotationProcessors that extend the {@link AnnotationProcessor}
 * are already marked as AnnotationProcessorService as the Annotation inherits.
 *
 * @author Philipp Sch&uuml;rmann
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
