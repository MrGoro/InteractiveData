package com.github.mrgoro.interactivedata.spring.boot.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mrgoro.interactivedata.spring.config.InteractiveDataConfiguration;
import com.github.mrgoro.interactivedata.spring.config.InteractiveDataProperties;
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
 * @author Philipp Sch&uuml;rmann
 */
@Configuration
public class SpringBootInteractiveDataConfiguration extends InteractiveDataConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "interactive-data")
    @Override
    public InteractiveDataProperties properties() {
        return super.properties();
    }
}
