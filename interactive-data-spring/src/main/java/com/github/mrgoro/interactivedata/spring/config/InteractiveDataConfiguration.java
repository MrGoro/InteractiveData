package com.github.mrgoro.interactivedata.spring.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.mrgoro.interactivedata.api.service.ChartDefinitionService;
import com.github.mrgoro.interactivedata.api.service.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base Configuration for interactive-data-spring
 *
 * @author Philipp Sch&uuml;rmann
 */
@Configuration
@ComponentScan({"com.github.mrgoro.interactivedata"})
public class InteractiveDataConfiguration {

    private static final Log log = LogFactory.getLog(InteractiveDataConfiguration.class);

    @Bean
    public InteractiveDataProperties properties() {
        InteractiveDataProperties properties = new InteractiveDataProperties();
        configureProperties(properties);
        return properties;
    }

    /**
     * Configure properties.
     *
     * Override this method to customize properties.
     *
     * @param properties Properties
     */
    public void configureProperties(InteractiveDataProperties properties) {}

    /**
     * The Jackson {@link ObjectMapper} used for serialization.
     *
     * @return Jackson Object Mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Customize object serialization and deserialization.
     *
     * Override this method to customize the object mapper.
     *
     * @param objectMapper Object mapper to configure
     */
    @Autowired(required = true)
    public void configureJackson(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new Swagger2JacksonModule());

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    @Bean
    public ChartDefinitionService chartDefinitionService(ServiceProvider serviceProvider) {
        log.info("Creating ChartDefinitionService");
        return new ChartDefinitionService(serviceProvider);
    }

}
