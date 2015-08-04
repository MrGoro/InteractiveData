package de.schuermann.interactivedata.spring.boot.autoconfigure;

import de.schuermann.interactivedata.spring.config.InteractiveDataConfiguration;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for InteractiveData Data Integration
 * integration.
 *
 * Once in effect, the auto-configuration allows to configure any property of
 * {@link InteractiveDataProperties} using the {@code interactive-data} prefix.
 *
 * @author Philipp Schürmann
 */
@Configuration
@ConditionalOnMissingBean(InteractiveDataConfiguration.class)
@ConditionalOnClass(InteractiveDataConfiguration.class)
@AutoConfigureAfter(HttpMessageConvertersAutoConfiguration.class)
@Import(SpringBootInteractiveDataConfiguration.class)
public class InteractiveDataAutoConfiguration {
}