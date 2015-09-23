package com.github.mrgoro.interactivedata.spring.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.mrgoro.interactivedata.api.service.ServiceProvider;
import com.github.mrgoro.interactivedata.api.service.ChartDefinitionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
     * Override this method to customize the object mapper.
     *
     * @return Jackson Object Mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        // Configure custom Modules
        configureJacksonObjectMapper(objectMapper);

        return objectMapper;
    }

    /**
     * Configure the Jackson {@link ObjectMapper} directly.
     *
     * @param objectMapper The {@literal ObjectMapper} to be used by the system.
     */
    protected void configureJacksonObjectMapper(ObjectMapper objectMapper) {}

    @Bean
    public ChartDefinitionService chartDefinitionService(ServiceProvider serviceProvider) {
        log.info("Creating ChartDefinitionService");
        return new ChartDefinitionService(serviceProvider);
    }

}
