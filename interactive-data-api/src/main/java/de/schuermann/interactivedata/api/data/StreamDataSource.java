package de.schuermann.interactivedata.api.data;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author Philipp Schürmann
 */
public abstract class StreamDataSource implements DataSource {

    protected abstract List<?> getData();

    @Override
    public List<DataObject> getData(DataRequest dataRequest) {
        List<?> dbResult = getData();
        return postProcess(getDataStream(dbResult), dataRequest);
    }

    protected Stream<DataObject> getDataStream(List<?> dataList) {
        return dataList.parallelStream()
                .map(DataObject::create);
    }

    protected List<DataObject> postProcess(Stream<DataObject> dataStream, DataRequest dataRequest) {
        Stream<DataObject> stream = dataStream;

        // Filter
        for(Filter<?> filter : dataRequest.getFilter()) {
            if(filter.shouldFilter()) {
                stream = stream.filter(filter.toPredicate());
            }
        }
        List<DataObject> filteredData = stream.collect(toList());

        // Granularities and functions
        List<DataObject> collect = dataRequest.getOperations().stream().flatMap(
                operation -> {
                    Map<Function, Map<Object, ?>> operationStream = new HashMap<Function, Map<Object, ?>>();
                    operation.getFunction().stream().forEach(function ->
                                    operationStream.put(
                                            function,
                                            filteredData.parallelStream().collect(
                                                    groupingBy(operation.getGranularity().toGroupFunction(), function.toCollector())
                                            )
                                    )
                    );
                    return processOperationResult(operation, operationStream).stream();
                }
        ).collect(toList());

        return collect;
    }

    private List<DataObject> processOperationResult(DataRequest.Operation operation, Map<Function, Map<Object, ?>> operationResult) {
        Map<Object, DataObject> result = new HashMap<>();
        for(Map.Entry<Function, Map<Object, ?>> functionResult : operationResult.entrySet()) {
            Function function = functionResult.getKey();
            for(Map.Entry<Object, ?> dataEntry : functionResult.getValue().entrySet()) {
                Object key = dataEntry.getKey();
                DataObject dataObject = result.get(key);
                if(dataObject == null) {
                    dataObject = DataObject.createEmpty();
                    result.put(key, dataObject);
                    dataObject.setProperty(operation.getGranularity().getFieldName(), key);
                }
                dataObject.setProperty(function.getFieldName(), dataEntry.getValue());
            }
        }
        return new ArrayList<>(result.values());
    }
}
