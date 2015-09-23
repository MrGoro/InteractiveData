package com.github.mrgoro.interactivedata.api.data.operations.granularity;

import com.github.mrgoro.interactivedata.api.data.operations.OperationData;
import com.github.mrgoro.interactivedata.api.data.operations.Operation;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;

import java.util.function.Function;

/**
 * Specification of a Granularity.
 * <p>
 * A granularity is capable of returning a different granularity levels of a single object. Granularity is used
 * when grouping multiple objects. Based on the level of granularity the amount of grouping is specified.
 * {@link Function Functions} are used when to aggregate fields that are not grouped.
 * <p>
 * Functions are parameterized with request data and options. Options are set once. Request data originates from
 * a specific request and is therefore set per request. The Framework automatically populates the data for options
 * and requests. Functions only have to specify the form of the data with custom {@link OperationData} classes.
 *
 * @param <D> Type of the Request Data Object
 * @param <O> Type of the Options Object
 * @author Philipp Sch&uuml;rman
 */
public abstract class Granularity<D extends OperationData, O extends OperationData> extends Operation<D, O> {

    /**
     * Transform an object to the specified granularity level.
     * <p>
     * Grouping is based on this object. Equal objects are grouped.
     *
     * @param dataObject Object to transform
     * @return Object at granularity level
     */
    protected abstract Object group(DataObject dataObject);

    /**
     * Get a {@link Function} for grouping from the Granularity.
     *
     * @return Granularity group function
     */
    public Function<DataObject, Object> toGroupFunction() {
        if(shouldOperate()) {
            return this::group;
        } else {
            return this::getProperty;
        }
    }

    public Granularity(String fieldName, Class fieldClass, D requestData, O options) {
        super(fieldName, fieldClass, requestData, options);
    }

}
