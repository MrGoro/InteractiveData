package com.github.mrgoro.interactivedata.api.data.operations;

import com.github.mrgoro.interactivedata.api.data.operations.granularity.Granularity;
import com.github.mrgoro.interactivedata.api.service.DataMapperService;
import com.github.mrgoro.interactivedata.api.util.ReflectionUtil;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.operations.filter.Filter;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Generic definition of an operation. An Operation does "something" with a field in a data set. As an operation
 * is abstract it cannot directly be instantiated.
 * <p>
 * Operation is the base type of {@link Filter}, {@link Granularity} and {@link Function}.
 * <p>
 * Operations are parameterized with request data and options. Options are set once. Request data originates from
 * a specific request and is therefore set per request. {@link Operation.Builder} helps creating these instances
 * with changing request data. The Framework automatically populates the data for options
 * and requests. Filters only have to specify the form of the data with custom {@link OperationData} classes.
 *
 * @param <D> Type of the Request Data Object
 * @param <O> Type of the Options Object
 * @author Philipp Sch&uuml;rmann
 */
public abstract class Operation<D extends OperationData, O extends OperationData> {

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

    protected Object getProperty(DataObject dataObject) {
        return dataObject.getOptionalProperty(getFieldName()).orElse(null);
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


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " +
                "RequestData[" + getRequestData().toString() + "] " +
                "Options[" + getRequestData().toString() + "]";
    }

    /**
     * Builder for easy creating of {@link Operation Operations} with often changing request data.
     * <p>
     * Allows to only set the changing request data to the builder for creating the {@link Operation} object.
     * <p>
     * Note: Performance of reflective operations is improved as calls are cached.
     *
     * @param <F> Type of the Operation
     */
    public static class Builder<F extends Operation<?, ?>> {

        protected DataMapperService dataMapperService;
        protected String fieldName;
        protected Class fieldClass;
        protected OperationData requestData;
        protected OperationData options;
        protected Class<F> operationClass;
        protected Class<? extends OperationData> requestDataClass;
        protected Class<? extends OperationData> optionsClass;
        protected Constructor<F> constructor;
        protected OperationData defaultRequestData;
        protected OperationData defaultOptions;

        /**
         * Create a new Instance of a Operation.Builder.
         *
         * @param operationClass    Class of the Operation
         * @param dataMapperService Mapper used for setting request and options data
         * @param <X>               Type of the Operation
         * @return New Operation.Builder Instance
         */
        @SuppressWarnings("unchecked")
        public static <X extends Operation<?, ?>> Builder<X> getInstance(Class<X> operationClass, DataMapperService dataMapperService) {
            ParameterizedType parameterizedType = (ParameterizedType) operationClass.getGenericSuperclass();
            if (parameterizedType.getActualTypeArguments().length != 2) {
                throw new IllegalArgumentException("Cannot instantiate builder of class [" + operationClass.getSimpleName() + "] " +
                        "because it has not exactly one generic superclass");
            }
            Type genericType1 = parameterizedType.getActualTypeArguments()[0];
            Type genericType2 = parameterizedType.getActualTypeArguments()[1];
            Class<? extends OperationData> requestDataClass = (Class<? extends OperationData>) genericType1;
            Class<? extends OperationData> optionsClass = (Class<? extends OperationData>) genericType2;
            return new Builder<>(operationClass, requestDataClass, optionsClass, dataMapperService);
        }

        private Builder(Class<F> operationClass, Class<? extends OperationData> requestDataClass, Class<? extends OperationData> optionsClass, DataMapperService dataMapperService) {
            this.dataMapperService = dataMapperService;
            if (operationClass == null || requestDataClass == null || optionsClass == null) {
                throw new IllegalArgumentException("Operation class or request data class or options class are not allowed to be null for a Filter.Builder");
            }
            this.operationClass = operationClass;
            this.requestDataClass = requestDataClass;
            this.optionsClass = optionsClass;
            this.defaultRequestData = ReflectionUtil.getInstance(requestDataClass);
            this.defaultOptions = ReflectionUtil.getInstance(optionsClass);
            try {
                this.constructor = operationClass.getConstructor(String.class, Class.class, getRequestDataClass(), getOptionsClass());
                this.constructor.newInstance(null, null, null, null); // Check if Constructor is available during initialization.
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Operation [" + operationClass.getSimpleName() + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + requestDataClass.getSimpleName() + ", " + optionsClass.getSimpleName() + ")", e);
            }
        }

        /**
         * Specify the name of the field
         *
         * @param fieldName name of the field
         * @return Builder
         */
        public Builder<F> fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        /**
         * Specify the class of the field.
         *
         * @param fieldClass class of the field
         * @return Builder
         */
        public Builder<F> fieldClass(Class fieldClass) {
            this.fieldClass = fieldClass;
            return this;
        }

        /**
         * Specify the request data.
         *
         * @param requestData Request data
         * @return Builder
         */
        public Builder<F> requestData(OperationData requestData) {
            this.requestData = requestData;
            return this;
        }

        /**
         * Specify the request data as a {@code Map<String, String[]>}. A {@link DataMapperService} is used to
         * map the data to the request object.
         *
         * @param data Request data as map
         * @return Builder
         */
        public Builder<F> requestData(Map<String, String[]> data) {
            this.requestData = this.dataMapperService.mapMultiDataOnObject(data, requestDataClass);
            if(this.requestData == null) {
                this.requestData = this.defaultRequestData;
            }
            return this;
        }

        /**
         * Specify the options.
         *
         * @param options Options
         * @return Builder
         */
        public Builder<F> options(OperationData options) {
            this.options = options;
            return this;
        }

        /**
         * Specify the options as a {@code Map<String, String[]>}. A {@link DataMapperService} is used to
         * map the data to the options object.
         *
         * @param options Options as a map2
         * @return Builder
         */
        public Builder<F> options(Map<String, String> options) {
            this.options = this.dataMapperService.mapDataOnObject(options, optionsClass);
            if(this.options == null) {
                this.options = this.defaultOptions;
            }
            return this;
        }

        /**
         * Build the Operations instance using the previously specified information.
         *
         * Note: This uses a cached constructor for better performance.
         *
         * @throws IllegalArgumentException when information specified are incorrect and instance cannot be built
         * @return Instance of the Operation
         */
        public F build() throws IllegalArgumentException {
            // Check parameters before building
            if (getFieldName() == null || getFieldName().isEmpty()) {
                throw new IllegalArgumentException("Cannot build Operation, FieldName cannot be null or empty");
            }
            if (getFieldClass() == null) {
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

        protected String getFieldName() {
            return fieldName;
        }

        protected Class getFieldClass() {
            return fieldClass;
        }

        protected OperationData getRequestData() {
            return requestData;
        }

        protected OperationData getOptions() {
            return options;
        }

        public Class<? extends OperationData> getOptionsClass() {
            return optionsClass;
        }

        public Class<F> getOperationClass() {
            return operationClass;
        }

        public Class<? extends OperationData> getRequestDataClass() {
            return requestDataClass;
        }
    }
}
