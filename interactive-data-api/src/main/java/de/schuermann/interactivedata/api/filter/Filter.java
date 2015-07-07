package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Sch�rmann
 */
public interface Filter<T extends FilterData> {

    void doFilter(T filterData);

}
