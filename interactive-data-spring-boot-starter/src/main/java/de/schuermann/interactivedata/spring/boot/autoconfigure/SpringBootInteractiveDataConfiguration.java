package de.schuermann.interactivedata.spring.boot.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.schuermann.interactivedata.spring.config.InteractiveDataConfiguration;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * A specialized {@link InteractiveDataConfiguration} that applies configuration items
 * from the {@code interactive-data} namespace. Also configures Jackson if it's available.
 * <p>
 * Favor an extension of this class instead of extending directly from
 * {@link InteractiveDataConfiguration}.
 *
 * @author Philipp Schürmann
 */
@Configuration
public class SpringBootInteractiveDataConfiguration extends InteractiveDataConfiguration {

    @Autowired(required = false)
    private Jackson2ObjectMapperBuilder objectMapperBuilder;

    @Bean
    @ConfigurationProperties(prefix = "interactive-data")
    @Override
    public InteractiveDataProperties properties() {
        return super.properties();
    }

    @Override
    protected void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        if (this.objectMapperBuilder != null) {
            this.objectMapperBuilder.configure(objectMapper);
        }
    }

}
