package de.schuermann.interactivedata.api.data;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class DataRequest {

    private AbstractChartDefinition chartDefinition;
    private List<Filter<?>> filter = new ArrayList<>();
    private List<Operation> operations = new ArrayList<>();

    public AbstractChartDefinition getChartDefinition() {
        return chartDefinition;
    }

    public List<Filter<?>> getFilter() {
        return filter;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setChartDefinition(AbstractChartDefinition chartDefinition) {
        this.chartDefinition = chartDefinition;
    }

    public void setFilter(List<Filter<?>> filter) {
        this.filter = filter;
    }

    public void addFilter(Filter filter) {
        this.filter.add(filter);
    }

    public void addOperation(Granularity<?> granularity, Function<?>... functions) {
        // Check if all functions belong to another field than granularity
        if(Arrays.stream(functions).noneMatch(function -> function.getFieldName().equals(granularity.getFieldName()))) {
            operations.add(new Operation(granularity, functions));
        } else {
            throw new IllegalArgumentException("Granularity and Function cannot be used on the same field [" + granularity.getFieldName() + "]");
        }
    }

    public class Operation {
        private Granularity<?> granularity;
        private List<Function<?>> function;

        private Operation(Granularity<?> granularity, Function<?>... functions) {
            this.granularity = granularity;
            this.function = Arrays.asList(functions);
        }

        public Granularity<?> getGranularity() {
            return granularity;
        }

        public List<Function<?>> getFunctions() {
            return function;
        }
    }
}
