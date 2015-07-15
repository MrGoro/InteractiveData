package de.schuermann.interactivedata.spring.config;

/**
 * Interactive Data Configurations Options
 *
 * @author Philipp Schürmann
 */
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