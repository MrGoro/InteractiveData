package com.github.mrgoro.interactivedata.api.handler;

import com.github.mrgoro.interactivedata.api.chart.data.ChartData;
import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.ChartPostProcessor;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.OperationInfo;
import com.github.mrgoro.interactivedata.api.data.DataRequest;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.operations.Operation;
import com.github.mrgoro.interactivedata.api.data.operations.filter.Filter;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Function;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.Granularity;
import com.github.mrgoro.interactivedata.api.data.source.DataSource;
import com.github.mrgoro.interactivedata.api.service.DataMapperService;
import com.github.mrgoro.interactivedata.api.service.ServiceProvider;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartRequestHandlerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Basic Implementation of a RequestHandler that is able to process a request for a specific chart.
 * <p>
 * Abstract implementation that solves basic functionality. Should be extended for specialized purpose.
 * Every type of chart ({@link AbstractChartDefinition ChartDefinition}) must have a corresponding RequestHandler.
 * <p>
 * Every RequestHandler must at least override the convert method to support its special conversion from
 * generic data to the specific ChartData.
 *
 * @author Philipp Sch&uuml;rmann
 */
@ChartRequestHandlerService
@Named
public abstract class ChartRequestHandler<T extends AbstractChartDefinition<?, D>, D extends ChartData> {

    private static final Log log = LogFactory.getLog(ChartRequestHandler.class);

    private DataMapperService dataMapperService;
    private ServiceProvider serviceProvider;

    private T chartDefinition;
    private DataSource dataSource;
    private ChartPostProcessor<D, ?> postProcessor;
    private List<Operation.Builder<? extends Filter<?, ?>>> filterBuilder = new ArrayList<>();
    private Map<Operation.Builder<? extends Granularity<?, ?>>, List<Operation.Builder<? extends Function<?, ?>>>> operationBuilder = new HashMap<>();

    @Inject
    public ChartRequestHandler(DataMapperService dataMapperService, ServiceProvider serviceProvider) {
        this.dataMapperService = dataMapperService;
        this.serviceProvider = serviceProvider;
    }

    /**
     * Initialize a ChartRequestHandler with the information specified by the appropriate
     * {@link AbstractChartDefinition ChartDefinition}.
     * <p>
     * This will initialize {@link Operation.Builder} for quick
     * creating of {@link DataRequest DataRequests} with {@link Filter Filters}, {@link Granularity Granularities} and
     * {@link Function Functions} populated with the parameters from the request.
     * <p>
     * This will also instantiate the {@link DataSource} with the class provided in the {@link AbstractChartDefinition}
     * using the {@link ServiceProvider}.
     *
     * @param chartDefinition ChartDefinition containing all information about the chart this RequestHandler should handle
     */
    public void initialize(T chartDefinition) {
        if (this.chartDefinition == null) {
            this.chartDefinition = chartDefinition;

            this.filterBuilder = new ArrayList<>();
            this.operationBuilder = new HashMap<>();

            dataSource = serviceProvider.getDataSource(chartDefinition.getDataSource());
            postProcessor = chartDefinition.getChartPostProcessor();

            // Initialize Builders to speed up Filter/Granularity/Function creating
            // not needing Reflection at runtime / every request
            //
            // Filter.Builder
            chartDefinition.getFilters().stream()
                    .map(filterInfo ->
                            Operation.Builder.getInstance(filterInfo.getFilter(), dataMapperService)
                                    .fieldName(filterInfo.getFieldName())
                                    .fieldClass(filterInfo.getFieldClass())
                                    .options(filterInfo.getOptions()))
                    .collect(toList())
                    .forEach(filterBuilder::add);

            // Operations (Granularity.Builder, Function.Builder)
            for (OperationInfo operationInfo : chartDefinition.getOperations()) {
                Operation.Builder<? extends Granularity<?, ?>> granularityBuilder =
                        Operation.Builder.getInstance(operationInfo.getGranularity(), dataMapperService)
                                .fieldName(operationInfo.getFieldName())
                                .fieldClass(operationInfo.getFieldClass())
                                .options(operationInfo.getOptions());

                List<Operation.Builder<? extends Function<?, ?>>> functionBuilder = operationInfo.getFunctionInfos().stream()
                        .map(functionInfo ->
                                        Operation.Builder.getInstance(functionInfo.getFunction(), dataMapperService)
                                                .fieldName(functionInfo.getFieldName())
                                                .fieldClass(functionInfo.getFieldClass())
                                                .options(functionInfo.getOptions())
                        ).collect(toList());

                operationBuilder.put(granularityBuilder, functionBuilder);
            }
        }
    }

    /**
     * Handle the ChartRequest for data and return an Object that is completely processed through all steps.
     *
     * @param chartRequest ChartRequest
     * @return Object containing data
     */
    public Object handleDataRequest(ChartRequest chartRequest) {
        log.debug(chartRequest);

        // Get DataRequest from ChartRequest
        DataRequest dataRequest = getDataRequest(chartRequest);
        // Query DataSource for Data with DataRequest
        List<DataObject> chartData = getData(dataRequest);
        // Convert to specific ChartData with specialized Handler
        D specificChartData = convertData(chartData);
        // Post process before returning
        return postProcessor.process(specificChartData);
    }

    /**
     * Convert generic {@link DataObject DataObjects} into the specific {@link ChartData} used for the chart type.
     * <p>
     * As this method is abstract extending classes always have to provide an implementation of the method. The
     * Implementation has to transform the generic data to the type used for the chart type.
     *
     * @param chartData Generic data queried from DataSource
     * @return ChartData for the specific chart type
     */
    protected abstract D convertData(List<DataObject> chartData);

    protected List<DataObject> getData(DataRequest dataRequest) {
        return dataSource.getData(dataRequest);
    }

    /**
     * Get a {@link DataRequest} from a {@link ChartRequest}.
     * <p>
     * Uses {@link Operation.Builder} to populate request parameters
     * to the objects.
     *
     * @param chartRequest ChartRequest
     * @return DataRequest
     */
    protected DataRequest getDataRequest(ChartRequest chartRequest) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setChartDefinition(chartDefinition);
        dataRequest.setFilter(
                // Create Filter objects with request data
                filterBuilder.stream()
                        .map(builder -> builder.requestData(chartRequest.getData()).build())
                        .collect(toList())
        );

        // Create Operation object with request data for Granularity and Function
        for (Map.Entry<Operation.Builder<? extends Granularity<?, ?>>, List<Operation.Builder<? extends Function<?, ?>>>> entry : operationBuilder.entrySet()) {
            dataRequest.addOperation(
                    entry.getKey().requestData(chartRequest.getData()).build(),
                    entry.getValue().stream()
                            .map(builder -> builder.requestData(chartRequest.getData()).build())
                            .collect(toList())
            );
        }

        log.debug(dataRequest.toString());

        return dataRequest;
    }

    /**
     * Return any type providing the documentation for the chart type.
     * <p>
     * Default returns {@link AbstractChartDefinition ChartDefinition} from {@link #getChartDefinition()}.
     *
     * @return Object of documentation
     */
    public Object handleInfoRequest() {
        return getChartDefinition();
    }

    /**
     * Get the ChartDefinition the RequestHandler is initialized with.
     *
     * @return ChartDefinition
     */
    protected T getChartDefinition() {
        return chartDefinition;
    }

    /**
     * Get the ChartPostProcessor, the function that calls the annotated method.
     *
     * @return ChartPostProcessor
     */
    protected ChartPostProcessor<D, ?> getPostProcessor() {
        return postProcessor;
    }
}
