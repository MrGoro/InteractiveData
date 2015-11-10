package com.github.mrgoro.interactivedata.api.chart.definitions;

/**
 * Definition of the {@link com.github.mrgoro.interactivedata.api.service.annotations.ChartService ChartService}.
 *
 * Created by Philipp on 10.11.2015.
 */
public class ServiceDefinition {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceDefinition(String name) {
        this.name = name;
    }
}
