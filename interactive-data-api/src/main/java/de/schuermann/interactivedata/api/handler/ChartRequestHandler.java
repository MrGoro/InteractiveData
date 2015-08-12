package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.FilterData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic Implementation of a RequestHandler that is able to process a request for a specific chart.
 *
 * Abstract implementation that solves basic functionality. Should be extended for specialized purpose.
 * Every type of chart (ChartDefinition) must have a corresponding RequestHandler.
 *
 * Every RequestHandler must at least override the convert method to support its special conversion from
 * generic data to the specific ChartData.
 *
 * @author Philipp Schürmann
 */
@ChartRequestHandlerService
@Named
public abstract class ChartRequestHandler<T extends AbstractChartDefinition<?, D>, D extends ChartData> {

    private DataMapperService dataMapperService;
    private ServiceProvider serviceProvider;

    private T chartDefinition;
    private DataSource dataSource;
    private List<Filter.Builder> filterBuilder = new ArrayList<>();
    private ChartPostProcessor<D> postProcessor;

    @Inject
    public ChartRequestHandler(DataMapperService dataMapperService, ServiceProvider serviceProvider) {
        // Dependencies
        this.dataMapperService = dataMapperService;
        this.serviceProvider = serviceProvider;
    }

    public void setChartDefinition(T chartDefinition) {
        this.chartDefinition = chartDefinition;

        dataSource = serviceProvider.getDataSource(chartDefinition.getDataSource());
        postProcessor = chartDefinition.getChartPostProcessor();

        // Initialize Filter.Builder to speed up Filter creating not needing Reflection at runtime / every request
        chartDefinition.getDimensions().forEach(
                dimension -> dimension.getFilters().forEach(
                        filterClass ->
                                filterBuilder.add(
                                        Filter.Builder.getInstance(filterClass)
                                                .fieldName(dimension.getDataField())
                                                .fieldClass(dimension.getDataType())
                                )
                )
        );
    }

    public D handleDataRequest(Request request) {
        List<Filter> filters = getFilters(request);
        List<DataObject> chartData = getData(chartDefinition, filters);
        D specificChartData = convertData(chartData);
        return postProcessor.process(specificChartData);
    }

    protected abstract D convertData(List<DataObject> chartData);

    protected List<DataObject> getData(T chartDefinition, List<Filter> filters) {
        return dataSource.getData(chartDefinition, filters);
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    /**
     * Get a List of filters from the request using the Filter.Builders.
     * Filters will be populated with data from the request.
     *
     * @param request Request
     * @return List of Filters populated with Data
     */
    protected List<Filter> getFilters(Request request) {
        List<Filter> filters = new ArrayList<>();
        for(Filter.Builder builder : filterBuilder) {
            Class<? extends FilterData> filterDataClass = builder.getFilterDataClass();
            FilterData filterData = dataMapperService.mapDataOnObject(request.getData(), filterDataClass);
            filters.add(builder.filterData(filterData).build());
        }
        return filters;
    }

    public String handleInfoRequest() {
        return "Ganz viele Infos zum Chart mit dem Namen " + chartDefinition.getName();
    }
}
