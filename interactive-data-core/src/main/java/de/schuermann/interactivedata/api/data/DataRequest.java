package de.schuermann.interactivedata.api.data;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Philipp Sch&uuml;rmann
 */
public class DataRequest {

    private AbstractChartDefinition chartDefinition;
    private List<Filter<?,?>> filter = new ArrayList<>();
    private List<Operation> operations = new ArrayList<>();

    public AbstractChartDefinition getChartDefinition() {
        return chartDefinition;
    }

    public List<Filter<?,?>> getFilter() {
        return filter;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setChartDefinition(AbstractChartDefinition chartDefinition) {
        this.chartDefinition = chartDefinition;
    }

    public void setFilter(List<Filter<?,?>> filter) {
        this.filter = filter;
    }

    public void addFilter(Filter filter) {
        this.filter.add(filter);
    }

    public void addOperation(Granularity<?,?> granularity, List<Function<?,?>> functions) {
        // Check if all functions belong to another field than granularity
        if(functions.stream().noneMatch(function -> function.getFieldName().equals(granularity.getFieldName()))) {
            operations.add(new Operation(granularity, functions));
        } else {
            throw new IllegalArgumentException("Granularity and Function cannot be used on the same field [" + granularity.getFieldName() + "]");
        }
    }

    public void addOperation(Granularity<?,?> granularity, Function<?,?>... functions) {
        addOperation(granularity, Arrays.asList(functions));
    }

    public class Operation {
        private Granularity<?,?> granularity;
        private List<Function<?,?>> functions;

        private Operation(Granularity<?,?> granularity, List<Function<?,?>> functions) {
            this.granularity = granularity;
            this.functions = functions;
        }

        public Granularity<?,?> getGranularity() {
            return granularity;
        }

        public List<Function<?,?>> getFunctions() {
            return functions;
        }

        @Override
        public String toString() {
            return "Operation{" +
                    "granularity=" + granularity +
                    ", functions=" + functions +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DataRequest{" +
                "chartDefinition=" + chartDefinition +
                ", filter=" + filter +
                ", operations=" + operations +
                '}';
    }
}
