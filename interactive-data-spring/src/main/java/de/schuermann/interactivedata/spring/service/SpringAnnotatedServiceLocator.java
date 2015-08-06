package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.service.AnnotatedServiceLocator;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import de.schuermann.interactivedata.spring.util.AdvancedReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Philipp Schürmann
 */
@Service
public class SpringAnnotatedServiceLocator extends AnnotatedServiceLocator {

    private String path;

    @Autowired
    public SpringAnnotatedServiceLocator(InteractiveDataProperties properties) {
        path = properties.getPath();
    }

    @Override
    public Collection<Class<?>> getServices(Class<? extends Annotation> annotation) {
        return AdvancedReflectionUtil.findAnnotatedClasses(path, annotation);
    }
}
