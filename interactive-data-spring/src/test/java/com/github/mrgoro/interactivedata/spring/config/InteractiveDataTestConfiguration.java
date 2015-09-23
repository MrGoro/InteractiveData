package com.github.mrgoro.interactivedata.spring.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Configuration
public class InteractiveDataTestConfiguration extends InteractiveDataConfiguration {

    @Override
    public void configureProperties(InteractiveDataProperties properties) {
        properties.setPath("com.github.mrgoro.interactivedata");
    }
}
