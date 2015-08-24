package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyOperationData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Function that calculates the sum of numeric values.
 * <p>
 * Integer, Long and Double are supported.
 *
 * <blockquote>
 *     <b>Request Data:</b><br>
 *     none
 * </blockquote>
 * <blockquote>
 *     <b>Options:</b><br>
 *     none
 * </blockquote>
 *
 * @see Collectors#summarizingInt(ToIntFunction)
 * @see Collectors#summarizingLong(ToLongFunction)
 * @see Collectors#summarizingDouble(ToDoubleFunction)
 * @author Philipp Sch&uuml;rmann
 */
public class Sum extends Function<EmptyOperationData, EmptyOperationData> {

    public Sum(String fieldName, Class fieldClass, EmptyOperationData requestData, EmptyOperationData options) {
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
