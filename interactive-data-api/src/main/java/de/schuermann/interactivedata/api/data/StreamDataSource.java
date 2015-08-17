package de.schuermann.interactivedata.api.data;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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

        // Filter
        dataRequest.getFilter().stream().filter(Filter::shouldFilter).forEach(filter -> {
            dataStream.filter(filter.toPredicate());
        });

        // Granularities and functions
        List<Map<Object, DataObject>> grouped = new ArrayList<>();
        dataRequest.getOperations().stream().forEach(
            operation -> operation.getFunction().stream().forEach(
                function ->
                    grouped.add(
                        dataStream.collect(
                            groupingBy(operation.getGranularity().toGroupFunction(), function.toCollector())
                        )
                    )
            )
        );

        return dataStream.collect(toList());
    }
}
