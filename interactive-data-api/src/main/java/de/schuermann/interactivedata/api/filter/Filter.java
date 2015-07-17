package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Sch�rmann
 */
public interface Filter<D extends FilterData> {

    abstract void doFilter(D filterData);
}
