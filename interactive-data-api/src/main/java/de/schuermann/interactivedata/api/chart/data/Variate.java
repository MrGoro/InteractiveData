package de.schuermann.interactivedata.api.chart.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class Variate {

    private String name;

    private List<Object> dimensions = new ArrayList<>();

    public Variate(String name, List<Object> dimensions) {
        this.name = name;
        this.dimensions = dimensions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Object> dimensions) {
        this.dimensions = dimensions;
    }
}
