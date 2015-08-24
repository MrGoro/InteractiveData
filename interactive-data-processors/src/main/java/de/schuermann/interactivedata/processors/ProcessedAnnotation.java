package de.schuermann.interactivedata.processors;

import java.lang.annotation.*;

/**
 * Annotation to mark a class to be processed by the {@link ServiceAnnotationProcessor}.
 *
 * This annotation is only used internal. Service Annotation are already marked and as this annotation
 * is inherited it is not needed any further.
 *
 * @author Philipp Schürmann
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ProcessedAnnotation {
}
