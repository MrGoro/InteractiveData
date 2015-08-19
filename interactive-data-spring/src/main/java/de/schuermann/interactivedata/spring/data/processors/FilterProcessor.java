package de.schuermann.interactivedata.spring.data.processors;

import de.schuermann.interactivedata.api.data.operations.RequestData;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Interface that has to be implemented by every FilterProcessor
 *
 * @author Philipp Schürmann
 */
@Component
public interface FilterProcessor<T extends Filter<?,?>> {

    Predicate filter(Root root, CriteriaQuery<?> query, CriteriaBuilder cb, T filter);

}
