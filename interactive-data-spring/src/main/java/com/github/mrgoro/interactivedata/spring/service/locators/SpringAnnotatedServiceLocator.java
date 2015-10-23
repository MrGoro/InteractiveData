package com.github.mrgoro.interactivedata.spring.service.locators;

import com.github.mrgoro.interactivedata.api.service.AnnotatedServiceLocator;
import com.github.mrgoro.interactivedata.spring.config.InteractiveDataProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Service Locator that uses Springs ClassPathScanningCandidateComponentProvider to find services.
 *
 * @author Philipp Sch&uuml;rmann
 */
@Service
public class SpringAnnotatedServiceLocator extends AnnotatedServiceLocator {

    // Base path configuration for improved performance
    private String path;

    @Autowired
    public SpringAnnotatedServiceLocator(InteractiveDataProperties properties) {
        path = properties.getPath();
    }

    @Override
    public Collection<Class<?>> getServices(Class<? extends Annotation> annotation) {
        return ClassLocatorUtil.findAnnotatedClasses(path, annotation);
    }
}
