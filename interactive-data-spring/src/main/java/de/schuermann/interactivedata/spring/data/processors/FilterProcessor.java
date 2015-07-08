package de.schuermann.interactivedata.spring.data.processors;

import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;

/**
 * @author Philipp Schürmann
 */
public interface FilterProcessor<T extends Filter<? extends FilterData>> {

    void filter(T filter, FilterData data);

}
