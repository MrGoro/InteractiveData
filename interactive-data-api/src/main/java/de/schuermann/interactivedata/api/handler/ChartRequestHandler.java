package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.data.DataRequest;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
    private List<Operation.Builder<? extends Filter<?>>> filterBuilder;
    private List<Operation.Builder<? extends Granularity<?>>> granularityBuilder;
    private List<Operation.Builder<? extends Function<?>>> functionBuilder;
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

        // Initialize Builders to speed up Filter/Granularity/Function creating
        // not needing Reflection at runtime / every request
        filterBuilder = new ArrayList<>();
        granularityBuilder = new ArrayList<>();
        functionBuilder = new ArrayList<>();
        chartDefinition.getDimensions().forEach(
                dimension -> {
                    // Get Filter.Builder
                    dimension.getFilters().parallelStream()
                            .map(filterClass ->
                                    Operation.Builder.getInstance(filterClass, dataMapperService)
                                        .fieldName(dimension.getDataField())
                                        .fieldClass(dimension.getDataType())
                            )
                            .collect(toList())
                            .forEach(filterBuilder::add);

                    // Get Granularity.Builder
                    dimension.getGranularities().parallelStream()
                            .map(granularityClass ->
                                            Operation.Builder.getInstance(granularityClass, dataMapperService)
                                                    .fieldName(dimension.getDataField())
                                                    .fieldClass(dimension.getDataType())
                            )
                            .collect(toList())
                            .forEach(granularityBuilder::add);

                    // Get Function.Builder
                    dimension.getFunctions().parallelStream()
                            .map(functionClass ->
                                    Operation.Builder.getInstance(functionClass, dataMapperService)
                                            .fieldName(dimension.getDataField())
                                            .fieldClass(dimension.getDataType()))
                            .collect(toList())
                            .forEach(functionBuilder::add);
                }

        );
    }

    public D handleDataRequest(Request request) {
        List<Filter<?>> filters = getFilters(request);
        List<Granularity<?>> granularities = getGranularities(request);
        List<Function<?>> functions = getFunctions(request);
        List<DataObject> chartData = getData(chartDefinition, filters, granularities, functions);
        D specificChartData = convertData(chartData);
        return postProcessor.process(specificChartData);
    }

    protected abstract D convertData(List<DataObject> chartData);

    protected List<DataObject> getData(T chartDefinition, List<Filter<?>> filters, List<Granularity<?>> granularities, List<Function<?>> functions) {
        return dataSource.getData(getDataRequest(chartDefinition, filters, granularities, functions));
    }

    protected DataRequest getDataRequest(T chartDefinition, List<Filter<?>> filters,
                                         List<Granularity<?>> granularities, List<Function<?>> functions) {

        DataRequest dataRequest = new DataRequest();
        dataRequest.setChartDefinition(chartDefinition);
        dataRequest.setFilter(filters);
        for(Granularity<?> granularity : granularities) {
            List<Function<?>> relatedFunctions = functions.stream()
                    .filter(function -> !function.getFieldName().equals(granularity.getFieldName()))
                    .collect(toList());
            dataRequest.addOperation(granularity, relatedFunctions.toArray(new Function[relatedFunctions.size()]));
        }

        return dataRequest;
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    /**
     * Get a List of Filters from the request using the Filter.Builders.
     * Filters will be populated with data from the request.
     *
     * @param request Request
     * @return List of Filters populated with data
     */
    protected List<Filter<?>> getFilters(Request request) {
        return filterBuilder.stream()
                .map(builder -> builder.requestData(request.getData()).build())
                .collect(toList());
    }

    /**
     * Get a List of Granularities from the request using Granularity.Builder.
     * Granularities will be populated with data from the request.
     *
     * @param request Request
     * @return List of Granularities populated with data
     */
    protected List<Granularity<?>> getGranularities(Request request) {
        return granularityBuilder.stream()
                .map(builder -> builder.requestData(request.getData()).build())
                .collect(toList());
    }

    /**
     * Get a List of Functions from the request using Function.Builder.
     * Functions will be populated with data from the request.
     *
     * @param request Request
     * @return List of Function populated with data
     */
    private List<Function<?>> getFunctions(Request request) {
        return functionBuilder.stream()
                .map(builder -> builder.requestData(request.getData()).build())
                .collect(toList());
    }

    public String handleInfoRequest() {
        return "Ganz viele Infos zum Chart mit dem Namen " + chartDefinition.getName();
    }
}
