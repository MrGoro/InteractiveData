package de.schuermann.interactivedata.spring.config;

import de.schuermann.interactivedata.spring.InteractiveData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base Configuration for interactive-data-spring
 *
 * @author Philipp Schürmann
 */
@Configuration
@EnableConfigurationProperties(InteractiveDataProperties.class)
@ComponentScan(basePackageClasses = InteractiveData.class)
public class InteractiveDataConfiguration {

    @Autowired
    InteractiveDataProperties properties;
}
