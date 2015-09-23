package com.github.mrgoro.interactivedata.api.service.annotations;

import com.github.mrgoro.interactivedata.processors.ProcessedAnnotation;

import java.lang.annotation.*;

/**
 * Annotation indicating the root of a collection of corresponding APIs.
 *
 * @author Philipp Sch&uuml;rmann
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ProcessedAnnotation
public @interface ChartService {

    /**
     * Name of the service.
     * <p>
     * The name is part of the identification pattern for the api. The identification of a chart api has the format
     * of %Service-Name%/%Chart-Name%.
     *
     * @return Path String
     */
    String value();
}
