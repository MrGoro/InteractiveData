package de.schuermann.interactivedata.api.chart.definitions;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.data.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractChartDefinition<T extends AbstractDimension, D extends ChartData> {

    private String name;
    private Class<? extends DataSource> dataSource;
    private List<T> dimensions;
    private ChartPostProcessor<D> chartPostProcessor;

    protected AbstractChartDefinition(String name, Class<? extends DataSource> dataSource) {
        this.name = name;
        this.dataSource = dataSource;
        this.dimensions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Class<? extends DataSource> getDataSource() {
        return dataSource;
    }

    public List<T> getDimensions() {
        return dimensions;
    }

    protected void addDimension(T dimension) {
        dimensions.add(dimension);
    }

    public ChartPostProcessor<D> getChartPostProcessor() {
        return chartPostProcessor;
    }

    public void setChartPostProcessor(ChartPostProcessor<D> chartPostProcessor) {
        this.chartPostProcessor = chartPostProcessor;
    }
}
