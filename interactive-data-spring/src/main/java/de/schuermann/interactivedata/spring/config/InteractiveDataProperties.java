package de.schuermann.interactivedata.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Philipp Schürmann
 */
@ConfigurationProperties(prefix="interactive-data")
public class InteractiveDataProperties {

    // TODO Bad performance => find better path
    private String path = "";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
