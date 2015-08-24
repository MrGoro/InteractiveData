package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.operations.EmptyOperationData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

/**
 * Granularity that groups identical objects. There is only one level of granularity.
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
 * @author Philipp Sch&uuml;rmann
 */
public class DistinctGranularity extends Granularity<EmptyOperationData, EmptyOperationData> {

    public DistinctGranularity(String fieldName, Class fieldClass, EmptyOperationData requestData, EmptyOperationData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    protected Object group(DataObject dataObject) {
        return dataObject.getProperty(getFieldName(), getFieldClass());
    }
}
