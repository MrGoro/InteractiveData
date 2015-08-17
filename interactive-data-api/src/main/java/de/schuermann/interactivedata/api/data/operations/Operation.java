package de.schuermann.interactivedata.api.data.operations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Philipp Schürmann
 */
public abstract class Operation<D extends RequestData> {

    protected String fieldName;
    protected Class fieldClass;
    protected D requestData;

    public Operation(String fieldName, Class fieldClass, D requestData) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.requestData = requestData;
    }

    public String getFieldName() {
        return fieldName;
    }

    protected void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    protected void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }

    public D getRequestData() {
        return requestData;
    }

    protected void setRequestData(D requestData) {
        this.requestData = requestData;
    }

    public static class Builder<F extends Operation<D>, D extends RequestData>  {

        protected String fieldName;
        protected Class fieldClass;
        protected D requestData;
        protected Class<F> operationClass;
        protected Class<D> requestDataClass;
        protected Constructor<F> constructor;

        /**
         * Create a new Instance of a FilterBuilder
         *
         * @param operationClass Class of the Filter
         * @param <X> Filter
         * @param <Y> FilterData
         * @return New FilterBuilder Instance
         */
        @SuppressWarnings("unchecked")
        public static <X extends Operation<Y>, Y extends RequestData> Builder<X, Y> getInstance(Class<X> operationClass) {
            ParameterizedType parameterizedType = (ParameterizedType)operationClass.getGenericSuperclass();
            Type genericType = parameterizedType.getActualTypeArguments()[0]; // TODO Check Array Size / Null Pointer
            Class<Y> requestDataClass = (Class<Y>) genericType;
            return new Builder<>(operationClass, requestDataClass);
        }
        private Builder(Class<F> operationClass, Class<D> requestDataClass) {
            if(operationClass == null || requestDataClass == null) {
                throw new IllegalArgumentException("FilterClass or FilterDataClass are not allowed to be null for a Filter.Builder");
            }
            this.operationClass = operationClass;
            this.requestDataClass = requestDataClass;
            try {
                this.constructor = operationClass.getConstructor(String.class, Class.class, getRequestDataClass());
                this.constructor.newInstance(null, null, null); // Check if Constructor is available during initialization.
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Operation [" + operationClass.getSimpleName() + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + requestDataClass.getSimpleName() + ")", e);
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
            this.requestData = filterData;
            return this;
        }
        protected String getFieldName() {
            return fieldName;
        }
        protected Class getFieldClass() {
            return fieldClass;
        }
        protected D getRequestData() {
            return requestData;
        }
        public Class<F> getOperationClass() {
            return operationClass;
        }
        public Class<D> getRequestDataClass() {
            return requestDataClass;
        }
        public F build() {
            // Check parameters before building
            if(getFieldName() == null || getFieldName().isEmpty()) {
                throw new IllegalArgumentException("Cannot build Operation, FieldName cannot be null or empty");
            }
            if(getFieldClass() == null) {
                throw new IllegalArgumentException("Cannot build Operation, FieldClass cannot be null");
            }
            try {
                return constructor.newInstance(getFieldName(), getFieldClass(), getRequestData());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // should not be thrown as tested in constructor
                throw new IllegalArgumentException("Operation [" + operationClass.getSimpleName() + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + requestDataClass.getSimpleName() + ")", e);
            }
        }
    }
}
