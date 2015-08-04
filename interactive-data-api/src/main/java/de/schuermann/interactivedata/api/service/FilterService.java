package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.processors.ProcessedAnnotation;

import java.lang.annotation.*;

/**
 * @author Philipp Schürmann
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ProcessedAnnotation
public @interface FilterService {
}
