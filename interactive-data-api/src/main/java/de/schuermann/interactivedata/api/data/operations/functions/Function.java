package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.RequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.stream.Collector;

/**
 * @author Philipp Schürmann
 */
public abstract class Function<D extends RequestData> extends Operation<D> {

    private String targetFieldName;

    public Function(String fieldName, Class fieldClass, D requestData) {
        super(fieldName, fieldClass, requestData);
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

    public abstract Collector<DataObject, ?, ?> toCollector();
}
