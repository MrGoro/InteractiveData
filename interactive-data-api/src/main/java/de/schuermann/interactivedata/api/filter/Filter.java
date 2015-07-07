package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Schürmann
 */
public interface Filter<T extends FilterData> {

    void doFilter(T filterData);

}
