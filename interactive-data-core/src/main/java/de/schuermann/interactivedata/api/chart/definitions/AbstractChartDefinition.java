package de.schuermann.interactivedata.api.chart.definitions;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.operations.FilterInfo;
import de.schuermann.interactivedata.api.chart.definitions.operations.OperationInfo;
import de.schuermann.interactivedata.api.data.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractChartDefinition<T extends AbstractDimension, D extends ChartData> {

    private String name;
    private Class<? extends DataSource> dataSource;
    private ChartPostProcessor<D> chartPostProcessor;

    private List<T> dimensions = new ArrayList<>();
    private List<FilterInfo> filters = new ArrayList<>();
    private List<OperationInfo> operations = new ArrayList<>();

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

    public void setName(String name) {
        this.name = name;
    }

    public void setDataSource(Class<? extends DataSource> dataSource) {
        this.dataSource = dataSource;
    }

    public void setDimensions(List<T> dimensions) {
        this.dimensions = dimensions;
    }

    public void setFilters(List<FilterInfo> filters) {
        this.filters = filters;
    }

    public void setOperations(List<OperationInfo> operations) {
        this.operations = operations;
    }

    public void setChartPostProcessor(ChartPostProcessor<D> chartPostProcessor) {
        this.chartPostProcessor = chartPostProcessor;
    }

    public List<FilterInfo> getFilters() {
        return filters;
    }

    public List<OperationInfo> getOperations() {
        return operations;
    }

    public void addFilter(FilterInfo filterInfo) {
        filters.add(filterInfo);
    }

    public void addOperation(OperationInfo operationInfo) {
        operations.add(operationInfo);
    }








}
