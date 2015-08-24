package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Philipp Schürmann
 */
public class Sum extends Function<EmptyRequestData, EmptyRequestData> {

    public Sum(String fieldName, Class fieldClass, EmptyRequestData requestData, EmptyRequestData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        if(getFieldClass() == Long.class) {
            return Collectors.summingLong(value -> value.getProperty(getFieldName(), Long.class));
        } if(getFieldClass() == Integer.class) {
            return Collectors.summarizingInt(value -> value.getProperty(getFieldName(), Integer.class));
        } if(getFieldClass() == Double.class) {
            return Collectors.summingDouble(value -> value.getProperty(getFieldName(), Double.class));
        } else {
            throw new ChartDefinitionException("Field [ " + getFieldName() + "] " +
                    "of Class [" + getFieldClass().getSimpleName() + "] " +
                    "not supported for sum calculation");
        }
    }
}
