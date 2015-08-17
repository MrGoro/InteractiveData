package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.annotations.FilterService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;

/**
 * Base for every Filter. Used for storing information of the filter that cannot be accessed / changed
 * from a request.
 *
 * @author Philipp Schürmann
 */
@FilterService
public abstract class Filter<D extends FilterData> {

    protected String fieldName;
    protected Class fieldClass;
    protected D filterData;

    /**
     * Builder for quick creation of Filter objects when data changes.
     *
     * @param <F> Filter
     * @param <D> FilterData
     */
    public static class Builder<F extends Filter<D>, D extends FilterData> {
        protected String fieldName;
        protected Class fieldClass;
        protected D filterData;
        protected Class<F> filterClass;
        protected Class<D> filterDataClass;
        protected Constructor<F> constructor;

        /**
         * Create a new Instance of a FilterBuilder
         *
         * @param filterClass Class of the Filter
         * @param <X> Filter
         * @param <Y> FilterData
         * @return New FilterBuilder Instance
         */
        @SuppressWarnings("unchecked")
        public static <X extends Filter<Y>, Y extends FilterData> Builder<X, Y> getInstance(Class<X> filterClass) {
            ParameterizedType parameterizedType = (ParameterizedType)filterClass.getGenericSuperclass();
            Type genericType = parameterizedType.getActualTypeArguments()[0]; // TODO Check Array Size / Null Pointer
            Class<Y> filterDataClass = (Class<Y>) genericType;
            return new Builder<>(filterClass, filterDataClass);
        }
        private Builder(Class<F> filterClass, Class<D> filterDataClass) {
            if(filterClass == null || filterDataClass == null) {
                throw new IllegalArgumentException("FilterClass or FilterDataClass are not allowed to be null for a Filter.Builder");
            }
            this.filterClass = filterClass;
            this.filterDataClass = filterDataClass;
            try {
                this.constructor = filterClass.getConstructor(String.class, Class.class, getFilterDataClass());
                this.constructor.newInstance(null, null, null); // Check if Constructor is available during initialization.
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Filter [" + filterClass + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + filterDataClass.getName() + ")", e);
            }
        }
        public Builder<F, D> fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }
        public Builder<F, D> fieldClass(Class fieldClass) {
            this.fieldClass = fieldClass;
            return this;
        }
        public Builder<F, D> filterData(D filterData) {
            this.filterData = filterData;
            return this;
        }
        protected String getFieldName() {
            return fieldName;
        }
        protected Class getFieldClass() {
            return fieldClass;
        }
        protected D getFilterData() {
            return filterData;
        }
        public Class<F> getFilterClass() {
            return filterClass;
        }
        public Class<D> getFilterDataClass() {
            return filterDataClass;
        }
        public F build() {
            // Check parameters before building
            if(getFieldName() == null || getFieldName().isEmpty()) {
                throw new IllegalArgumentException("Cannot build Filter, FieldName cannot be null or empty");
            }
            if(getFieldClass() == null) {
                throw new IllegalArgumentException("Cannot build Filter, FieldClass cannot be null");
            }
            try {
                return constructor.newInstance(getFieldName(), getFieldClass(), getFilterData());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // should not be thrown as tested in constructor
                throw new IllegalArgumentException("Filter does not provide a Constructor with appropriate visibility of form " +
                        "(String, " + getFilterData().getClass().getName() + ")");
            }
        }
    }

    public Filter(String fieldName, Class fieldClass, D filterData) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.filterData = filterData;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    public D getFilterData() {
        return filterData;
    }

    protected void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    protected void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }

    protected void setFilterData(D filterData) {
        this.filterData = filterData;
    }

    /**
     * Get a Predicate that is capable of filtering using this filters information.
     *
     * @return Predicate
     */
    public Predicate<? super DataObject> toPredicate() {
        return new FilterPredicate(this);
    }

    /**
     * Checks if the filter does have data so it does want to filter.
     * If false no filtering is needed at all and should not be done.
     *
     * @return true if filter wants to filter, false if not
     */
    public boolean shouldFilter() {
        return getFilterData() != null && getFilterData().shouldFilter();
    }

    /**
     * Test if the given Data object is suitable for current filter.
     *
     * @param t DataObject
     * @return true if object has to be included, false if not
     */
    protected abstract boolean test(DataObject t);

    private static class FilterPredicate implements Predicate<DataObject> {

        private Filter filter;

        public FilterPredicate(Filter filter) {
            this.filter = filter;
        }

        @Override
        public boolean test(DataObject t) {
            return filter.test(t);
        }
    }
}
