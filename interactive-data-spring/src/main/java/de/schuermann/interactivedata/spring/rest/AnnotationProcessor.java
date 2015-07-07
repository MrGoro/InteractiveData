package de.schuermann.interactivedata.spring.rest;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * @author Philipp Schürmann
 */
public interface AnnotationProcessor<T extends Annotation> {

    org.glassfish.jersey.server.model.Resource process(String name, T annotation);

}
