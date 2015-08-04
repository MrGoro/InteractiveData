package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.processors.ProcessedAnnotation;

import java.lang.annotation.*;

/**
 * Annotation indicating the root of a collection of corresponding APIs.
 *
 * @author Philipp Schürmann
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ProcessedAnnotation
public @interface ChartService {

    /**
     * Path (URL) the API will be available at.
     *
     * @return Path String
     */
    String value();

}
