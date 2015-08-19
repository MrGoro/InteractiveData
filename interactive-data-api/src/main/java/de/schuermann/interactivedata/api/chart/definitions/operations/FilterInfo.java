package de.schuermann.interactivedata.api.chart.definitions.operations;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;

public class FilterInfo extends AbstractFieldInfo {
    private Class<? extends Filter<?>> filter;

    public Class<? extends Filter<?>> getFilter() {
        return filter;
    }

    public void setFilter(Class<? extends Filter<?>> filter) {
        this.filter = filter;
    }
}