package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.data.DataRequest;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.FilterData;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.functions.FunctionData;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;
import de.schuermann.interactivedata.api.data.operations.granularity.GranularityData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
    private List<Filter.Builder<? extends Filter, ? extends FilterData>> filterBuilder = new ArrayList<>();
    private List<Granularity.Builder<? extends Granularity, ? extends GranularityData>> granularityBuilder = new ArrayList<>();
    private List<Function.Builder<? extends Function, ? extends FunctionData>> functionBuilder = new ArrayList<>();
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
        chartDefinition.getDimensions().forEach(
                dimension -> {
                    // Get Filter.Builder
                    filterBuilder = dimension.getFilters().parallelStream()
                            .map(filterClass ->
                                Filter.Builder.getInstance(filterClass)
                                            .fieldName(dimension.getDataField())
                                            .fieldClass(dimension.getDataType())
                            )
                            .collect(toList());

                    // Get Granularity.Builder
                    granularityBuilder = dimension.getGranularities().parallelStream()
                            .map(granularityClass ->
                                Granularity.Builder.getInstance(granularityClass)
                                            .fieldName(dimension.getDataField())
                                            .fieldClass(dimension.getDataType())
                            )
                            .collect(toList());

                    // Get Function.Builder
                    functionBuilder = dimension.getFunctions().parallelStream()
                            .map(functionClass ->
                                Function.Builder.getInstance(functionClass)
                                                .fieldName(dimension.getDataField())
                                                .fieldClass(dimension.getDataType()))
                            .collect(toList());
                }

        );
    }

    public D handleDataRequest(Request request) {
        List<Filter> filters = getFilters(request);
        List<Granularity> granularities = getGranularities(request);
        List<Function> functions = getFunctions(request);
        List<DataObject> chartData = getData(chartDefinition, filters, granularities, functions);
        D specificChartData = convertData(chartData);
        return postProcessor.process(specificChartData);
    }

    protected abstract D convertData(List<DataObject> chartData);

    protected List<DataObject> getData(T chartDefinition, List<Filter> filters, List<Granularity> granularities, List<Function> functions) {
        return dataSource.getData(getDataRequest(chartDefinition, filters, granularities, functions));
    }

    protected DataRequest getDataRequest(AbstractChartDefinition chartDefinition, List<Filter> filters,
                                         List<Granularity> granularities, List<Function> functions) {

        DataRequest dataRequest = new DataRequest();
        dataRequest.setChartDefinition(getChartDefinition());
        dataRequest.setFilter(filters);
        for(Granularity granularity : granularities) {
            List<Function> relatedFunctions = functions.stream()
                    .filter(function -> function.getFieldName().equals(granularity.getFieldName()))
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
    protected List<Filter> getFilters(Request request) {
        List<Filter> filters = new ArrayList<>();
        for(Filter.Builder builder : filterBuilder) {
            Class<? extends FilterData> filterDataClass = builder.getFilterDataClass();
            FilterData filterData = dataMapperService.mapDataOnObject(request.getData(), filterDataClass);
            filters.add(builder.filterData(filterData).build());
        }
        return filters;
    }

    /**
     * Get a List of Granularities from the request using Granularity.Builder.
     * Granularities will be populated with data from the request.
     *
     * @param request Request
     * @return List of Granularities populated with data
     */
    protected List<Granularity> getGranularities(Request request) {
        List<Granularity> granularities = new ArrayList<>();
        for(Granularity.Builder builder : granularityBuilder) {
            Class<? extends GranularityData> granularityDataClass = builder.getGranularityDataClass();
            GranularityData filterData = dataMapperService.mapDataOnObject(request.getData(), granularityDataClass);
            granularities.add(builder.granularityData(filterData).build());
        }
        return granularities;
    }

    /**
     * Get a List of Functions from the request using Function.Builder.
     * Functions will be populated with data from the request.
     *
     * @param request Request
     * @return List of Function populated with data
     */
    private List<Function> getFunctions(Request request) {
        List<Function> functions = new ArrayList<>();
        for(Function.Builder builder : functionBuilder) {
            Class<? extends FunctionData> functionDataClass = builder.getFunctionDataClass();
            FunctionData functionData = dataMapperService.mapDataOnObject(request.getData(), functionDataClass);
            functions.add(builder.functionData(functionData).build());
        }
        return functions;
    }

    public String handleInfoRequest() {
        return "Ganz viele Infos zum Chart mit dem Namen " + chartDefinition.getName();
    }
}
