package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Philipp Schürmann
 */
public class Average extends Function<EmptyRequestData, EmptyRequestData> {

    public Average(String fieldName, Class fieldClass, EmptyRequestData requestData, EmptyRequestData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        if(getFieldClass() == Long.class) {
            return Collectors.averagingLong(value -> value.getProperty(getFieldName(), Long.class));
        } if(getFieldClass() == Integer.class) {
            return Collectors.averagingInt(value -> value.getProperty(getFieldName(), Integer.class));
        } if(getFieldClass() == Double.class) {
            return Collectors.averagingDouble(value -> value.getProperty(getFieldName(), Double.class));
        } else {
            throw new ChartDefinitionException("Field [ " + getFieldName() + "] " +
                    "of Class [" + getFieldClass().getSimpleName() + "] " +
                    "not supported for average calculation");
        }
    }
}
