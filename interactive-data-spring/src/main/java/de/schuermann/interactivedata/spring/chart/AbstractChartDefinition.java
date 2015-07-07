package de.schuermann.interactivedata.spring.chart;

import de.schuermann.interactivedata.api.data.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractChartDefinition<T extends AbstractDimension> {

    private String name;
    private DataSource dataSource;
    private List<T> dimensions;

    protected AbstractChartDefinition(String name, DataSource dataSource) {
        this.name = name;
        this.dataSource = dataSource;
        this.dimensions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public List<T> getDimensions() {
        return dimensions;
    }

    protected void addDimension(T dimension) {
        dimensions.add(dimension);
    }
}
