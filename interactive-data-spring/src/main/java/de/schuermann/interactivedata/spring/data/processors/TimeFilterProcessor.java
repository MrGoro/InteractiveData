package de.schuermann.interactivedata.spring.data.processors;

import de.schuermann.interactivedata.api.filter.FilterData;
import de.schuermann.interactivedata.api.filter.TimeFilter;
import de.schuermann.interactivedata.api.filter.TimeFilterData;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Philipp Schürmann
 */
public class TimeFilterProcessor implements FilterProcessor<TimeFilter> {

    @Override
    public Specification filter(TimeFilter filter, FilterData data) {
        TimeFilterData timeFilterData = (TimeFilterData) data;
        return filter(timeFilterData);
    }

    public static Specification<?> filter(TimeFilterData filterData) {
        return (root, query, cb) -> {
            return cb.conjunction();
        };
    }
}
