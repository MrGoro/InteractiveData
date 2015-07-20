package de.schuermann.interactivedata.spring.data.processors;

import de.schuermann.interactivedata.api.filter.TimeFilter;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Philipp Schürmann
 */
@Component
public class TimeFilterProcessor implements FilterProcessor<TimeFilter> {

    @Override
    public Predicate filter(Root root, CriteriaQuery<?> query, CriteriaBuilder cb, TimeFilter filter) {
        //return filter(filter.getFilterData());
        return null;
    }
}
