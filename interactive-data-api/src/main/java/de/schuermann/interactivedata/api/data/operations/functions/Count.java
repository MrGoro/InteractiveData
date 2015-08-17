package de.schuermann.interactivedata.api.data.operations.functions;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Philipp Schürmann
 */
public class Count extends Function<EmptyFunctionData> {

    public Count(String fieldName, Class fieldClass, EmptyFunctionData functionData) {
        super(fieldName, fieldClass, functionData);
    }

    @Override
    public <T> Collector<T, ?, ?> toCollector() {
        return Collectors.counting();
    }

}
