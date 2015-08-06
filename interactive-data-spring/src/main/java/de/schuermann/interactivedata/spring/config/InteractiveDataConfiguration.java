package de.schuermann.interactivedata.spring.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.schuermann.interactivedata.spring.InteractiveData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base Configuration for interactive-data-spring
 *
 * @author Philipp Schürmann
 */
@Configuration
@ComponentScan(basePackageClasses = InteractiveData.class)
public class InteractiveDataConfiguration {

    @Bean
    public InteractiveDataProperties properties() {
        InteractiveDataProperties properties = new InteractiveDataProperties();
        configureProperties(properties);
        return properties;
    }

    /**
     * Configure properties
     *
     * @param properties
     */
    public void configureProperties(InteractiveDataProperties properties) {}

    /**
     * The Jackson {@link ObjectMapper} used for serialization.
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JSR310Module());

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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

}
