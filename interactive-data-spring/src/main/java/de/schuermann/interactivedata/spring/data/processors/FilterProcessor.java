package de.schuermann.interactivedata.spring.data.processors;

import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface that has to be implemented by every FilterProcessor
 *
 * @author Philipp Schürmann
 */
public interface FilterProcessor<T extends Filter<? extends FilterData>> {

    Specification filter(T filter, FilterData data);

}
