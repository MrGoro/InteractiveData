package de.schuermann.interactivedata.api.service.annotations;

import de.schuermann.interactivedata.processors.ProcessedAnnotation;

import java.lang.annotation.*;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ProcessedAnnotation
public @interface ChartRequestHandlerService {

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
