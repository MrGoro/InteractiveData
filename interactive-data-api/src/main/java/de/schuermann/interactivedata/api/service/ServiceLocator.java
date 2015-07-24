package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;

/**
 * @author Philipp Schürmann
 */
public interface ServiceLocator {

    interface FilterFactory {
        <F extends Filter<D>, D extends FilterData> F newInstance(Class<F> filterClass);
    }

    interface FilterBuilder<F extends Filter<D>, D extends FilterData> {
        FilterBuilder setFieldName(String fieldName);
        FilterBuilder setFilterData(D filterData);
        F build();
    }

    FilterFactory getFilterFactory();
}
