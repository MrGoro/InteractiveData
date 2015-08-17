package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.RequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Philipp Schürmann
 */
public class Average extends Function<EmptyRequestData> {

    public Average(String fieldName, Class fieldClass, EmptyRequestData requestData) {
        super(fieldName, fieldClass, requestData);
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
            throw new IllegalArgumentException("Field of Class [" + getFieldClass().getSimpleName()
                    + "] not supported for average calculation");
        }
    }
}
