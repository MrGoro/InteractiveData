package de.schuermann.interactivedata.api.data.operations;

import de.schuermann.interactivedata.api.service.DataMapperService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public abstract class Operation<D extends RequestData>  {

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

    public static class Builder<F extends Operation<?>>  {

        protected DataMapperService dataMapperService;
        protected String fieldName;
        protected Class fieldClass;
        protected RequestData requestData;
        protected Class<F> operationClass;
        protected Class<? extends RequestData> requestDataClass;
        protected Constructor<F> constructor;

        /**
         * Create a new Instance of a Operation.Builder
         *
         * @param operationClass Class of the Operation
         * @param <X> Type of the Operation
         * @return New Operation.Builder Instance
         */
        @SuppressWarnings("unchecked")
        public static <X extends Operation<?>> Builder<X> getInstance(Class<X> operationClass, DataMapperService dataMapperService) {
            ParameterizedType parameterizedType = (ParameterizedType)operationClass.getGenericSuperclass();
            Type genericType = parameterizedType.getActualTypeArguments()[0]; // TODO Check Array Size / Null Pointer
            Class<? extends RequestData> requestDataClass = (Class<? extends RequestData>) genericType;
            return new Builder<>(operationClass, requestDataClass, dataMapperService);
        }
        private Builder(Class<F> operationClass, Class<? extends RequestData> requestDataClass, DataMapperService dataMapperService) {
            this.dataMapperService = dataMapperService;
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
        public Builder<F> fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }
        public Builder<F> fieldClass(Class fieldClass) {
            this.fieldClass = fieldClass;
            return this;
        }
        public Builder<F> requestData(RequestData requestData) {
            this.requestData = requestData;
            return this;
        }
        public Builder<F> requestData(Map<String, String[]> data) {
            requestData = this.dataMapperService.mapDataOnObject(data, requestDataClass);
            return this;
        }
        protected String getFieldName() {
            return fieldName;
        }
        protected Class getFieldClass() {
            return fieldClass;
        }
        protected RequestData getRequestData() {
            return requestData;
        }
        public Class<F> getOperationClass() {
            return operationClass;
        }
        public Class<? extends RequestData> getRequestDataClass() {
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
