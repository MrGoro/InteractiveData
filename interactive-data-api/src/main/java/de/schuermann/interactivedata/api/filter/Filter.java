package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Sch�rmann
 */
public interface Filter<D extends FilterData> extends Cloneable {

    void setFieldName(String fieldName);

    String getFieldName();

    void setFilterData(D filterData);

    D getFilterData();

    Filter clone() throws CloneNotSupportedException;

}
