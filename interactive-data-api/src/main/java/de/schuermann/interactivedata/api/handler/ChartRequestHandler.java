package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.AbstractDimension;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.FilterData;
import de.schuermann.interactivedata.api.service.ServiceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class ChartRequestHandler<T extends AbstractDimension, D extends ChartData> {

    private String name;
    private DataSource dataSource;
    private Map<Class<? extends Filter>, Filter.Builder> filterBuilder = new HashMap<>();
    private ChartPostProcessor<D> postProcessor;

    public ChartRequestHandler(ServiceProvider serviceProvider, AbstractChartDefinition<T, D> chartDefinition) {
        name = chartDefinition.getName();
        dataSource = serviceProvider.getDataSource(chartDefinition.getDataSource());
        // TODO Refactor PostProcessor Initialization to Service Provider
        postProcessor = chartDefinition.getChartPostProcessor();
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
        return postProcessor.process(null);
    }

    public String handleInfoRequest() {
        return "Ganz viele Infos zum Chart mit dem Namen " + name;
    }
}
