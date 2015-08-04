package de.schuermann.interactivedata.spring.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author Philipp Sch�rmann
 */
@Configuration
public class InteractiveDataTestConfiguration extends InteractiveDataConfiguration {

    @Override
    public void configureProperties(InteractiveDataProperties properties) {
        properties.setPath("de.schuermann.interactivedata.spring");
    }
}
