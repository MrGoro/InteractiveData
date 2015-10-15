package com.github.mrgoro.interactivedata.api.service;

import com.github.mrgoro.interactivedata.processors.ServiceClassLocator;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Service Locator that uses the JSR-269 Pluggable Annotation Processing API
 * to find services.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class AnnotatedJSR269ServiceLocator extends AnnotatedServiceLocator {

    @Override
    public Collection<Class<?>> getServices(Class<? extends Annotation> annotation) {
        return ServiceClassLocator.getAnnotated(annotation);
    }
}