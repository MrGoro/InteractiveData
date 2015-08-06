package de.schuermann.interactivedata.spring.service.locators;

import de.schuermann.interactivedata.api.service.AnnotatedServiceLocator;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Service Locator that uses Springs ClassPathScanningCandidateComponentProvider to find Classes.
 *
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
        return ClassLocatorUtil.findAnnotatedClasses(path, annotation);
    }
}
