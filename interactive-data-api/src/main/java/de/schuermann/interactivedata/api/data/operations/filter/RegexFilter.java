package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.reflection.DataObject;

/**
 * @author Philipp Schürmann
 */
public class RegexFilter extends Filter<RegexFilterData> {

    public RegexFilter(String fieldName, Class fieldClass, RegexFilterData requestData) {
        super(fieldName, fieldClass, requestData);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        return dataObject.getProperty(getFieldName(), String.class).matches(getRequestData().getRegex());
    }
}
