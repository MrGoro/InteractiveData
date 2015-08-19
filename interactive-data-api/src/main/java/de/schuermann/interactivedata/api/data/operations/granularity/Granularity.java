package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.RequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.function.Function;

/**
 * @author Philipp Schürmann
 */
public abstract class Granularity<D extends RequestData, O extends RequestData> extends Operation<D, O> {

    public Granularity(String fieldName, Class fieldClass, D requestData, O options) {
        super(fieldName, fieldClass, requestData, options);
    }

    protected abstract Object group(DataObject dataObject);

    public Function<DataObject, Object> toGroupFunction() {
        if(shouldOperate()) {
            return new GroupFunction(this);
        } else {
            return dataObject -> dataObject.getProperty(getFieldName());
        }
    }

    private class GroupFunction implements Function<DataObject, Object> {

        private Granularity granularity;

        private GroupFunction(Granularity granularity) {
            this.granularity = granularity;
        }

        @Override
        public Object apply(DataObject dataObject) {
            return granularity.group(dataObject);
        }
    }

}
