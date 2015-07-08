package de.schuermann.interactivedata.spring.data.processors;

import de.schuermann.interactivedata.api.filter.FilterData;
import de.schuermann.interactivedata.api.filter.TimeFilter;
import de.schuermann.interactivedata.api.filter.TimeFilterData;

/**
 * @author Philipp Schürmann
 */
public class TimeFilterProcessor implements FilterProcessor<TimeFilter> {

    @Override
    public void filter(TimeFilter filter, FilterData data) {
        TimeFilterData timeFilterData = (TimeFilterData) data;
    }
}
