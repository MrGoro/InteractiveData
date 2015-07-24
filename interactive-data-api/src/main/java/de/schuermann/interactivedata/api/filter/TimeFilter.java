package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Schürmann
 */
public class TimeFilter extends Filter<TimeFilterData> {

    public TimeFilter(String fieldName) {
        super(fieldName);
    }

    public TimeFilter(String fieldName, TimeFilterData filterData) {
        super(fieldName, filterData);
    }
}
