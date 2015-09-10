package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.OperationData;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;
import de.schuermann.interactivedata.api.data.bean.DataObject;

import java.util.stream.Collector;

/**
 * Specification of a Function.
 * <p>
 * A function is capable of aggregating multiple objects to a single object. They are used when grouping multiple
 * objects (see {@link Granularity}) to aggregate fields that are not grouped.
 * <p>
 * Functions are parameterized with request data and options. Options are set once. Request data originates from
 * a specific request and is therefore set per request. The Framework automatically populates the data for options
 * and requests. Functions only have to specify the form of the data with custom {@link OperationData} classes.
 *
 * @param <D> Type of the Request Data Object
 * @param <O> Type of the Options Object
 * @author Philipp Sch&uuml;rman
 */
public abstract class Function<D extends OperationData, O extends OperationData> extends Operation<D, O> {

    private String targetFieldName;
    
    /**
     * Collector that is capable of aggregating multiple values to a single value.
     *
     * @return Collector for aggregation
     */
    public abstract Collector<DataObject, ?, ?> toCollector();

    public Function(String fieldName, Class fieldClass, D requestData, O options) {
        super(fieldName, fieldClass, requestData, options);
    }

    public String getTargetFieldName() {
        if(targetFieldName == null || targetFieldName.isEmpty()) {
            return getFieldName() + "." + getClass().getSimpleName().toLowerCase();
        }
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }

}
