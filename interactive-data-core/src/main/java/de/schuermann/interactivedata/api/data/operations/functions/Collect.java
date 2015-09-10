package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.EmptyOperationData;
import de.schuermann.interactivedata.api.data.bean.DataObject;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;

/**
 * Function that aggregates items by collecting an transforming them to a {@link java.util.List}.
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
 * @see Concatenation
 * @author Philipp Sch&uuml;rmann
 */
public class Collect extends Function<EmptyOperationData, EmptyOperationData> {

    public Collect(String fieldName, Class fieldClass, EmptyOperationData requestData, EmptyOperationData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        return mapping(this::getProperty, Collectors.toList());
    }
}
