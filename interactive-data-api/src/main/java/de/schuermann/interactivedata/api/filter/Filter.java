package de.schuermann.interactivedata.api.filter;

/**
 * @author Philipp Schürmann
 */
public abstract class Filter<D extends FilterData> {

    protected String fieldName;
    protected D filterData;

    public abstract static class Builder<D extends FilterData, X extends Filter<D>> {
        protected String fieldName;
        protected D filterData;
        public abstract Builder getInstance();
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
        public void setFilterData(D filterData) {
            this.filterData = filterData;
        }
        public abstract X build();
    }

    public Filter(String fieldName) {
        this.fieldName = fieldName;
    }

    public Filter(String fieldName, D filterData) {
        this.fieldName = fieldName;
        this.filterData = filterData;
    }

    public String getFieldName() {
        return fieldName;
    }

    public D getFilterData() {
        return filterData;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFilterData(D filterData) {
        this.filterData = filterData;
    }

    // TODO Cloneable
    @Override
    public Filter clone() throws CloneNotSupportedException {
        return (Filter) super.clone();
    }
}
