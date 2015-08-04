package de.schuermann.interactivedata.processors;

import java.lang.annotation.*;

/**
 * @author Philipp Schürmann
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ProcessedAnnotation {
}
