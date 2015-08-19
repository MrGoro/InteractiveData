package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;

/**
 * @author Philipp Schürmann
 */
public class Collect extends Function<EmptyRequestData, EmptyRequestData> {

    public Collect(String fieldName, Class fieldClass, EmptyRequestData requestData, EmptyRequestData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        return mapping(dataObject -> dataObject.getProperty(getFieldName(), getFieldClass()), Collectors.toList());
    }
}
