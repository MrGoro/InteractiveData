package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.processors.ServiceClassLocator;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Philipp Sch&uuml;rmann
 */
public class AnnotatedJSR269ServiceLocator extends AnnotatedServiceLocator {

    @Override
    public Collection<Class<?>> getServices(Class<? extends Annotation> annotation) {
        return ServiceClassLocator.getAnnotated(annotation);
    }

}
