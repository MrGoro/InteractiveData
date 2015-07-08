package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Schürmann
 */
public interface Filter<D extends FilterData> {

    void doFilter(D filterData);

}
