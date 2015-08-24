package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.EmptyOperationData;
import de.schuermann.interactivedata.api.data.operations.OperationData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

/**
 * Filter that applies a regular expression for filtering.
 *
 * <blockquote>
 *     <b>Request Data:</b><br>
 *     regex - /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/ (matches an email)
 * </blockquote>
 * <blockquote>
 *     <b>Options:</b><br>
 *     none
 * </blockquote>
 *
 * @see java.util.regex.Pattern
 * @author Philipp Sch&uuml;rmann
 */
public class RegexFilter extends Filter<RegexFilter.RegexFilterData, EmptyOperationData> {

    public RegexFilter(String fieldName, Class fieldClass, RegexFilterData requestData, EmptyOperationData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        // apply the regex on the data
        return dataObject.getProperty(getFieldName(), String.class).matches(getRequestData().getRegex());
    }

    /**
     * Request data for the {@link RegexFilter}.
     *
     * @see RegexFilter
     */
    public static class RegexFilterData implements OperationData {

        private String regex;

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        @Override
        public boolean hasData() {
            return regex != null && !regex.isEmpty();
        }

        @Override
        public String toString() {
            return "RegexFilterData{" +
                    "regex='" + regex + '\'' +
                    '}';
        }
    }
}
