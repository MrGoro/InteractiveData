package de.schuermann.interactivedata.api.data.operations;

import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * @author Philipp Schürmann
 */
public abstract class Operation<D extends RequestData, O extends RequestData>  {

    protected String fieldName;
    protected Class fieldClass;
    protected D requestData;
    protected O options;

    public Operation(String fieldName, Class fieldClass, D requestData, O options) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.requestData = requestData;
        this.options = options;
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

    public O getOptions() {
        return options;
    }

    public void setOptions(O options) {
        this.options = options;
    }

    public boolean shouldOperate() {
        return getRequestData() != null && getRequestData().hasData();
    }

    public static class Builder<F extends Operation<?, ?>>  {

        protected DataMapperService dataMapperService;
        protected String fieldName;
        protected Class fieldClass;
        protected RequestData requestData;
        protected RequestData options;
        protected Class<F> operationClass;
        protected Class<? extends RequestData> requestDataClass;
        protected Class<? extends RequestData> optionsClass;
        protected Constructor<F> constructor;

        /**
         * Create a new Instance of a Operation.Builder
         *
         * @param operationClass Class of the Operation
         * @param <X> Type of the Operation
         * @return New Operation.Builder Instance
         */
        @SuppressWarnings("unchecked")
        public static <X extends Operation<?, ?>> Builder<X> getInstance(Class<X> operationClass, DataMapperService dataMapperService) {
            ParameterizedType parameterizedType = (ParameterizedType) operationClass.getGenericSuperclass();
            if(parameterizedType.getActualTypeArguments().length != 2) {
                throw new IllegalArgumentException("Cannot instantiate builder of class [" + operationClass.getSimpleName() + "] " +
                        "because it has not exactly one generic superclass");
            }
            Type genericType1 = parameterizedType.getActualTypeArguments()[0];
            Type genericType2 = parameterizedType.getActualTypeArguments()[1];
            Class<? extends RequestData> requestDataClass = (Class<? extends RequestData>) genericType1;
            Class<? extends RequestData> optionsClass = (Class<? extends RequestData>) genericType2;
            return new Builder<>(operationClass, requestDataClass, optionsClass, dataMapperService);
        }
        private Builder(Class<F> operationClass, Class<? extends RequestData> requestDataClass, Class<? extends RequestData> optionsClass, DataMapperService dataMapperService) {
            this.dataMapperService = dataMapperService;
            if(operationClass == null || requestDataClass == null || optionsClass == null) {
                throw new IllegalArgumentException("Operation class or request data class or options class are not allowed to be null for a Filter.Builder");
            }
            this.operationClass = operationClass;
            this.requestDataClass = requestDataClass;
            this.optionsClass = optionsClass;
            try {
                this.constructor = operationClass.getConstructor(String.class, Class.class, getRequestDataClass(), getOptionsClass());
                this.constructor.newInstance(null, null, null, null); // Check if Constructor is available during initialization.
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Operation [" + operationClass.getSimpleName() + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + requestDataClass.getSimpleName() + ", " + optionsClass.getSimpleName() + ")", e);
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
            this.requestData = this.dataMapperService.mapMultiDataOnObject(data, requestDataClass);
            return this;
        }
        public Builder<F> options(Map<String, String> options) {
            this.options = this.dataMapperService.mapDataOnObject(options, optionsClass);
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
        protected RequestData getOptions() {
            return options;
        }

        public Class<? extends RequestData> getOptionsClass() {
            return optionsClass;
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
                return constructor.newInstance(getFieldName(), getFieldClass(), getRequestData(), getOptions());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // should not be thrown as tested in constructor
                throw new IllegalArgumentException("Operation [" + operationClass.getSimpleName() + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + requestDataClass.getSimpleName() + ", " + optionsClass.getSimpleName() + ")", e);
            }
        }
    }
}
