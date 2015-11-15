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

    /**
     * Provide Properties as a Bean.
     *
     * @return Properties for Interactive Data Framework with Spring
     */
    @Bean
    public InteractiveDataProperties properties() {
        InteractiveDataProperties properties = new InteractiveDataProperties();
        configureProperties(properties);
        return properties;
    }

    /**
     * Configure properties.
     * <p>
     * Override this method to customize properties.
     *
     * @param properties Properties
     */
    public void configureProperties(InteractiveDataProperties properties) {
    }

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
     * Configure an {@link ObjectMapper}.
     *
     * @param objectMapper Object mapper to configure
     */
    @Autowired(required = true)
    public final void configureJackson(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new Swagger2JacksonModule());

        configureObjectMapper(objectMapper);
    }

    /**
     * Custom configuration on {@link ObjectMapper}.
     * <p>
     * Override this method to customize the object mapper.
     *
     * @param objectMapper Object mapper to configure
     */
    protected void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Bean
    public ChartDefinitionService chartDefinitionService(ServiceProvider serviceProvider) {
        log.info("Creating ChartDefinitionService");
        return new ChartDefinitionService(serviceProvider);
    }

}
