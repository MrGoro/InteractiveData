package com.github.mrgoro.interactivedata.spring.data.processors;

import com.github.mrgoro.interactivedata.api.data.operations.filter.Filter;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Interface that has to be implemented by every FilterProcessor
 *
 * @author Philipp Sch&uuml;rmann
 */
@Component
public interface FilterToPredicateProcessor<T extends Filter<?,?>> {

    Predicate toPredicate(Root root, CriteriaQuery<?> query, CriteriaBuilder cb, T filter);

}
