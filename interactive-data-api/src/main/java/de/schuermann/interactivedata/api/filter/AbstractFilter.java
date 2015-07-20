package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractFilter<D extends FilterData> implements Filter<D> {

    protected String fieldName;
    protected D filterData;

    public AbstractFilter(String fieldName) {
        this.fieldName = fieldName;
    }

    public AbstractFilter(String fieldName, D filterData) {
        this.fieldName = fieldName;
        this.filterData = filterData;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public D getFilterData() {
        return filterData;
    }

    @Override
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void setFilterData(D filterData) {
        this.filterData = filterData;
    }

    @Override
    public Filter clone() throws CloneNotSupportedException {
        return (Filter) super.clone();
    }
}
