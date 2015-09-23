package com.github.mrgoro.interactivedata.api.data.operations.functions;

import com.github.mrgoro.interactivedata.api.data.operations.EmptyOperationData;
import com.github.mrgoro.interactivedata.api.util.exceptions.ChartDefinitionException;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;

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
            return Collectors.summingLong(value -> value.getOptionalProperty(getFieldName(), Long.class).orElse(0L));
        } if(getFieldClass() == Integer.class) {
            return Collectors.summarizingInt(value -> value.getOptionalProperty(getFieldName(), Integer.class).orElse(0));
        } if(getFieldClass() == Double.class) {
            return Collectors.summingDouble(value -> value.getOptionalProperty(getFieldName(), Double.class).orElse(0D));
        } else {
            throw new ChartDefinitionException("Field [ " + getFieldName() + "] " +
                    "of Type [" + getFieldClass().getSimpleName() + "] " +
                    "not supported for sum calculation");
        }
    }
}
