package de.schuermann.interactivedata.spring.sample.test;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.TimeFilterData;
import de.schuermann.interactivedata.api.service.annotations.FilterService;

// TODO Remove Test Class

/**
 * @author Philipp Schürmann
 */
@FilterService(5000)
public class TestFilterService extends Filter<TimeFilterData> {

    public TestFilterService(String fieldName) {
        super(fieldName);
    }

    public TestFilterService(String fieldName, TimeFilterData filterData) {
        super(fieldName, filterData);
    }

    @Override
    protected <T> boolean test(T t) {
        return false;
    }
}
