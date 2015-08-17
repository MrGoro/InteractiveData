package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * @author Philipp Schürmann
 */
public abstract class Granularity<D extends GranularityData> {

    protected String fieldName;
    protected Class fieldClass;
    protected D granularityData;

    /**
     * Builder for quick creation of Filter objects when data changes.
     *
     * @param <F> Filter
     * @param <D> FilterData
     */
    public static class Builder<F extends Granularity<D>, D extends GranularityData> {
        protected String fieldName;
        protected Class fieldClass;
        protected D granularityData;
        protected Class<F> granularityClass;
        protected Class<D> granularityDataClass;
        protected Constructor<F> constructor;

        /**
         * Create a new Instance of a Granularity.Builder
         *
         * @param granularityClass Class of the Granularity
         * @param <X> Type of the Granularity
         * @param <Y> Type of the GranularityData
         * @return New Granularity.Builder Instance
         */
        @SuppressWarnings("unchecked")
        public static <X extends Granularity<Y>, Y extends GranularityData> Builder<X, Y> getInstance(Class<X> granularityClass) {
            ParameterizedType parameterizedType = (ParameterizedType)granularityClass.getGenericSuperclass();
            Type genericType = parameterizedType.getActualTypeArguments()[0]; // TODO Check Array Size / Null Pointer
            Class<Y> filterDataClass = (Class<Y>) genericType;
            return new Builder<>(granularityClass, filterDataClass);
        }
        private Builder(Class<F> granularityClass, Class<D> granularityDataClass) {
            if(granularityClass == null || granularityDataClass == null) {
                throw new IllegalArgumentException("FilterClass or FilterDataClass are not allowed to be null for a Filter.Builder");
            }
            this.granularityClass = granularityClass;
            this.granularityDataClass = granularityDataClass;
            try {
                this.constructor = granularityClass.getConstructor(String.class, Class.class, getGranularityDataClass());
                this.constructor.newInstance(null, null, null); // Check if Constructor is available during initialization.
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Filter [" + granularityClass + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + granularityDataClass.getName() + ")", e);
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
        public Builder<F, D> granularityData(D granularityData) {
            this.granularityData = granularityData;
            return this;
        }
        protected String getFieldName() {
            return fieldName;
        }
        protected Class getFieldClass() {
            return fieldClass;
        }
        protected D getGranularityData() {
            return granularityData;
        }
        public Class<F> getGranularityClass() {
            return granularityClass;
        }
        public Class<D> getGranularityDataClass() {
            return granularityDataClass;
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
                return constructor.newInstance(getFieldName(), getFieldClass(), getGranularityData());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // should not be thrown as tested in constructor
                throw new IllegalArgumentException("Filter does not provide a Constructor with appropriate visibility of form " +
                        "(String, " + getGranularityData().getClass().getName() + ")");
            }
        }
    }

    public Granularity(String fieldName, Class fieldClass, D granularityData) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.granularityData = granularityData;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    protected void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }

    protected D getGranularityData() {
        return granularityData;
    }

    protected void setGranularityData(D granularityData) {
        this.granularityData = granularityData;
    }

    protected abstract Object group(DataObject dataObject);

    public boolean shouldGroup() {
        return getGranularityData() != null && getGranularityData().shouldGroup();
    }

    public Function<DataObject, Object> toGroupFunction() {
        return new GroupFunction(this);
    }

    private class GroupFunction implements Function<DataObject, Object> {

        private Granularity granularity;

        private GroupFunction(Granularity granularity) {
            this.granularity = granularity;
        }

        @Override
        public Object apply(DataObject dataObject) {
            return granularity.group(dataObject);
        }
    }


}
