package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

/**
 * @author Philipp Schürmann
 */
public class RegexFilter extends Filter<RegexFilterData, EmptyRequestData> {

    public RegexFilter(String fieldName, Class fieldClass, RegexFilterData requestData, EmptyRequestData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        return dataObject.getProperty(getFieldName(), String.class).matches(getRequestData().getRegex());
    }
}
