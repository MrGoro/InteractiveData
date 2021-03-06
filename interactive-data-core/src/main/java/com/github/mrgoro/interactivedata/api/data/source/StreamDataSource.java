package com.github.mrgoro.interactivedata.api.data.source;

import com.github.mrgoro.interactivedata.api.data.DataRequest;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.bean.DataObjectFactory;
import com.github.mrgoro.interactivedata.api.data.operations.filter.Filter;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Base Implementation of a {@link DataSource} using the Java 8 Stream API for data processing.
 *
 * @author Philipp Sch&uuml;rmann
 */
public abstract class StreamDataSource<T> implements DataSource {

    protected abstract Stream<T> getDataStream();

    protected Stream<DataObject> mapToDataObject(Stream<T> dataList) {
        return dataList.map(getMapper());
    }

    protected java.util.function.Function<T, DataObject> getMapper(){
        return DataObjectFactory::create;
    }

    protected DataRequest dataRequest;

    @Override
    public List<DataObject> getData(DataRequest dataRequest) {
        this.dataRequest = dataRequest;
        Stream<T> rawData = getDataStream();
        Stream<DataObject> dataStream = mapToDataObject(rawData);
        return postProcess(dataStream);
    }

    protected List<DataObject> postProcess(Stream<DataObject> dataStream) {
        Stream<DataObject> stream = dataStream;

        // Apply all filters to the stream
        for(Filter<?,?> filter : dataRequest.getFilter()) {
            if(filter.shouldOperate()) {
                stream = stream.filter(filter.toPredicate());
            }
        }

        // Process the stream with Operations (Map and Reduce)
        List<List<DataObject>> maps = processOperations(dataRequest, stream);
        if(maps != null && maps.size() > 0) {
            return maps.get(0);
        } else {
            return new ArrayList<>();
        }
    }

    private List<List<DataObject>> processOperations(DataRequest dataRequest, Stream<DataObject> dataStream) {
        List<List<DataObject>> result = new ArrayList<>();
        for(DataRequest.Operation operation : dataRequest.getOperations()) {
            // Get a Data Object builder from function field names
            String[] fieldNames = operation.getFunctions().stream().map(Function::getTargetFieldName).toArray(String[]::new);
            DataObjectBuilder dataObjectBuilder = new DataObjectBuilder(fieldNames);

            // Extract Collectors from functions
            List<Collector<DataObject, ?, ?>> functionCollectors = operation.getFunctions().stream().map(Function::toCollector).collect(toList());
            Collector<DataObject, ?, Object[]> multiCollector = getMultiCollector(functionCollectors);

            // Complex Group with multiple Functions
            Map<Object, DataObject> resultMap = dataStream.collect(
                    groupingBy(operation.getGranularity().toGroupFunction(),
                            Collectors.collectingAndThen(multiCollector, dataObjectBuilder::get))
            );

            // Build final Data Object containing group field (add to each element)
            result.add(resultMap.entrySet().stream()
                .map(e -> e.getValue().setPropertyAndGet(operation.getGranularity().getFieldName(), e.getKey()))
                .collect(toList())
            );
        }
        return result;
    }

    /**
     * Get a single Collector from a List of compatible collectors.
     *
     * Result of the collect operation will be a List containing the value of each single collector. Order is protected.
     *
     * @param collectors List of Collectors
     * @return Single Collector
     */
    @SuppressWarnings("unchecked")
    private Collector<DataObject, List<Object>, Object[]> getMultiCollector(List<Collector<DataObject, ?, ?>> collectors) {
        return Collector.of(
            () -> collectors.stream()
                    .map(Collector::supplier)
                    .map(Supplier::get)
                    .collect(toList()),
            (list, e) ->
                IntStream.range(0, collectors.size()).forEach(
                    i -> ((BiConsumer<Object, DataObject>) collectors.get(i).accumulator()).accept(list.get(i), e)
                ),
            (l1, l2) -> {
                IntStream.range(0, collectors.size()).forEach(
                        i -> l1.set(i, ((BinaryOperator<Object>) collectors.get(i).combiner()).apply(l1.get(i), l2.get(i))));
                return l1;
            },
            list -> {
                IntStream.range(0, collectors.size()).forEach(
                        i -> list.set(i, ((java.util.function.Function<Object, Object>) collectors.get(i).finisher()).apply(list.get(i))));
                return list.toArray();
            });
    }

    /**
     * Helper Class to simplify generation of DataObjects with static keys and changing values.
     *
     * Builder initialization defines the keys (static for the lifetime of the builder).
     *
     * Create multiple instances of DataObject with changing values by calling get() with the values.
     */
    class DataObjectBuilder {

        private String[] keys;
        private Object[] values;

        /**
         * Initialize a DataObjectBuilder with the keys.
         *
         * @param keys Keys of the DataObject
         */
        public DataObjectBuilder(String... keys) {
            this.keys = keys;
        }

        /**
         * Get a DataObject from the values matching the keys specified during Builder creation.
         *
         * @param values Arrays of values to populate the DataObject
         * @return DataObject
         * @throws IllegalArgumentException Exception when keys and values do not have the same size or at least one is null
         */
        public DataObject get(Object[] values) throws IllegalArgumentException {
            this.values = values;
            return build();
        }

        /**
         * Get a DataObject from the values matching the keys specified during Builder creation.
         *
         * @param list List of values to populate the DataObject
         * @return DataObject
         * @throws IllegalArgumentException Exception when keys and values do not have the same size or at least one is null
         */
        public DataObject get(List<Object> list) throws IllegalArgumentException {
            return get(list.toArray());
        }

        private DataObject build() throws IllegalArgumentException {
            if(keys == null || values == null || keys.length != values.length) {
                throw new IllegalArgumentException("Cannot build DataObject: Keys and Values must have the same size");
            }
            Map<String, Object> map = new HashMap<>();
            for(int i = 0; i < keys.length; i++) {
                map.put(keys[i], values[i]);
            }
            return DataObjectFactory.create(map);
        }
    }
}
