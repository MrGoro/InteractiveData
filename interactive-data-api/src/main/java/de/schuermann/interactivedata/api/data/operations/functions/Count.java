package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Philipp Schürmann
 */
public class Count extends Function<EmptyRequestData> {

    public Count(String fieldName, Class fieldClass, EmptyRequestData requestData) {
        super(fieldName, fieldClass, requestData);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        return Collectors.counting();
    }

}
