package de.schuermann.interactivedata.spring.data.processors;

import de.schuermann.interactivedata.api.data.operations.filter.TimeFilter;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Component
public class TimeFilterProcessor implements FilterToPredicateProcessor<TimeFilter> {

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery<?> query, CriteriaBuilder cb, TimeFilter filter) {
        return cb.between(root.get(filter.getFieldName()), filter.getRequestData().getStart(), filter.getRequestData().getEnd());
    }
}
