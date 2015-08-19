package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

/**
 * @author Philipp Schürmann
 */
public class DistinctGranularity extends Granularity<EmptyRequestData> {

    public DistinctGranularity(String fieldName, Class fieldClass, EmptyRequestData requestData) {
        super(fieldName, fieldClass, requestData);
    }

    @Override
    protected Object group(DataObject dataObject) {
        return dataObject.getProperty(getFieldName(), getFieldClass());
    }
}
