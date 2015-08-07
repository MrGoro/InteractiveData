package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.AbstractDimension;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.FilterData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class ChartRequestHandler<T extends AbstractDimension, D extends ChartData> {

    Map<Class<? extends Filter>, Filter.Builder> filterBuilder = new HashMap<>();

    public ChartRequestHandler(AbstractChartDefinition<T, D> chartDefinition) {
        chartDefinition.getDimensions().forEach(
            dimension -> dimension.getFilters().forEach(
                filterClass ->
                    filterBuilder.put(
                            filterClass,
                            Filter.Builder.getInstance(filterClass).fieldName(dimension.getDataField())
                    )
            )
        );
    }

    public List<Class<? extends FilterData>> getFilterDataClasses() {
        return null;
    }

    public D handleDataRequest(Request request) {
        return null;
    }

    public String handleInfoRequest() {
        return "Ganz viele Infos zum Chart";
    }
}
