package com.github.mrgoro.interactivedata.api.data.operations.granularity;

import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.operations.EmptyOperationData;

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

    /**
     * Method wont get called as {@link #shouldOperate()} returns true so that the identity is always used.
     *
     * @param dataObject Object to transform
     * @return Property of the DataObject
     */
    @Override
    protected Object group(DataObject dataObject) {
        return getProperty(dataObject);
    }

    @Override
    public boolean shouldOperate() {
        return false; // results in an implicit Distinct Granularity
    }
}
