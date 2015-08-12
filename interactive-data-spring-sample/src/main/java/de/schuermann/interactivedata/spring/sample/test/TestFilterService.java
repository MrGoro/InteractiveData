package de.schuermann.interactivedata.spring.sample.test;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.TimeFilterData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.annotations.FilterService;

// TODO Remove Test Class

/**
 * @author Philipp Schürmann
 */
@FilterService(5000)
public class TestFilterService extends Filter<TimeFilterData> {

    public TestFilterService(Class fieldClass, TimeFilterData filterData) {
        super("", fieldClass, filterData);
    }

    @Override
    protected boolean test(DataObject t) {
        return false;
    }
}
