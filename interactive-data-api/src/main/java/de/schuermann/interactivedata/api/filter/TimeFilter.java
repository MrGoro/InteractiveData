package de.schuermann.interactivedata.api.filter;

import de.schuermann.interactivedata.api.service.annotations.FilterService;

/**
 * @author Philipp Schürmann
 */
@FilterService
public class TimeFilter extends Filter<TimeFilterData> {

    public TimeFilter(String fieldName) {
        super(fieldName);
    }

    public TimeFilter(String fieldName, TimeFilterData filterData) {
        super(fieldName, filterData);
    }
}
