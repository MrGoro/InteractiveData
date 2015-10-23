package com.github.mrgoro.interactivedata.api.data.operations.functions;

import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.operations.EmptyOperationData;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Function that counts the number of objects that are aggregated.
 * <p>
 * This function is independent on the field name and field data type.
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
 * @see Collectors#counting()
 * @author Philipp Sch&uuml;rmann
 */
public class Count extends Function<EmptyOperationData, EmptyOperationData> {

    public Count(String fieldName, Class fieldClass, EmptyOperationData requestData, EmptyOperationData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        return Collectors.counting();
    }

}
