package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyOperationData;
import de.schuermann.interactivedata.api.data.bean.DataObject;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Function that calculates the average of numeric values.
 * <p>
 * Integer, Long and Double are supported. String is supported when parsable by {@link Double#parseDouble(String)}.
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
 * @see Collectors#averagingInt(ToIntFunction)
 * @see Collectors#averagingLong(ToLongFunction)
 * @see Collectors#averagingDouble(ToDoubleFunction)
 * @author Philipp Sch&uuml;rmann
 */
public class Average extends Function<EmptyOperationData, EmptyOperationData> {

    public Average(String fieldName, Class fieldClass, EmptyOperationData requestData, EmptyOperationData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        if(getFieldClass() == Long.class) {
            return Collectors.averagingLong(value -> value.getOptionalProperty(getFieldName(), Long.class).orElse(0L));
        } else if(getFieldClass() == Integer.class) {
            return Collectors.averagingInt(value -> value.getOptionalProperty(getFieldName(), Integer.class).orElse(0));
        } else if(getFieldClass() == Double.class) {
            return Collectors.averagingDouble(value -> value.getOptionalProperty(getFieldName(), Double.class).orElse(0D));
        } else if(getFieldClass() == String.class) {
            return Collectors.averagingDouble(value -> Double.parseDouble(value.getOptionalProperty(getFieldName(), String.class).orElse("0.0")));
        } else {
            throw new ChartDefinitionException("Field [ " + getFieldName() + "] " +
                    "of Type [" + getFieldClass().getSimpleName() + "] " +
                    "not supported for average calculation");
        }
    }
}