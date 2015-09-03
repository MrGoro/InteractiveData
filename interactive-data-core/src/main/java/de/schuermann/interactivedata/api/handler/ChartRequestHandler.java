package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.ChartPostProcessor;
import de.schuermann.interactivedata.api.chart.definitions.operations.OperationInfo;
import de.schuermann.interactivedata.api.data.DataRequest;
import de.schuermann.interactivedata.api.data.source.DataSource;
import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;
import de.schuermann.interactivedata.api.data.bean.DataObject;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;
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
 *
 * Abstract implementation that solves basic functionality. Should be extended for specialized purpose.
 * Every type of chart (ChartDefinition) must have a corresponding RequestHandler.
 *
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
    private List<Operation.Builder<? extends Filter<?,?>>> filterBuilder = new ArrayList<>();
    private Map<Operation.Builder<? extends Granularity<?,?>>, List<Operation.Builder<? extends Function<?,?>>>> operationBuilder = new HashMap<>();
    private ChartPostProcessor<D> postProcessor;

    @Inject
    public ChartRequestHandler(DataMapperService dataMapperService, ServiceProvider serviceProvider) {
        // Dependencies
        this.dataMapperService = dataMapperService;
        this.serviceProvider = serviceProvider;
    }

    public void initialize(T chartDefinition) {
        if(this.chartDefinition ==  null) {
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

    public D handleDataRequest(Request request) {
        log.debug(request);

        List<DataObject> chartData = getData(chartDefinition, request);
        D specificChartData = convertData(chartData);
        return postProcessor.process(specificChartData);
    }

    protected abstract D convertData(List<DataObject> chartData);

    protected List<DataObject> getData(T chartDefinition, Request request) {
        return dataSource.getData(getDataRequest(request));
    }

    protected DataRequest getDataRequest(Request request) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setChartDefinition(chartDefinition);
        dataRequest.setFilter(
            filterBuilder.stream()
                    .map(builder -> builder.requestData(request.getData()).build())
                    .collect(toList())
        );
        for(Map.Entry<Operation.Builder<? extends Granularity<?,?>>, List<Operation.Builder<? extends Function<?,?>>>> entry : operationBuilder.entrySet()) {
            dataRequest.addOperation(
                    entry.getKey().requestData(request.getData()).build(),
                    entry.getValue().stream()
                            .map(builder -> builder.requestData(request.getData()).build())
                            .collect(toList())
            );
        }
        log.debug(dataRequest.toString());
        return dataRequest;
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    public String handleInfoRequest() {
        return "Ganz viele Infos zum Chart mit dem Namen " + chartDefinition.getName();
    }
}
