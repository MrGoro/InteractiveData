package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.reflection.DataObject;

/**
 * @author Philipp Schürmann
 */
public class SearchFilter extends Filter<SearchFilterData> {

    public SearchFilter(String fieldName, Class fieldClass, SearchFilterData requestData) {
        super(fieldName, fieldClass, requestData);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        return dataObject.getProperty(getFieldName()).equals(getRequestData().getSearch()) ^ getRequestData().isInvert();
    }
}