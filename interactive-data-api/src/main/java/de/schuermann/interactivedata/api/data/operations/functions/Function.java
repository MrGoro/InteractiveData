package de.schuermann.interactivedata.api.data.operations.functions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Collector;

/**
 * @author Philipp Schürmann
 */
public abstract class Function<D extends FunctionData> {

    protected String fieldName;
    protected Class fieldClass;
    protected D functionData;


    /**
     * Builder for quick creation of Function objects when data changes.
     *
     * @param <F> Type of the Function
     * @param <D> Type of the FunctionData
     */
    public static class Builder<F extends Function<D>, D extends FunctionData> {
        protected String fieldName;
        protected Class fieldClass;
        protected D functionData;
        protected Class<F> functionClass;
        protected Class<D> functionDataClass;
        protected Constructor<F> constructor;

        /**
         * Create a new Instance of a Function.Builder
         *
         * @param functionClass Class of the Function
         * @param <X> Type of the Function
         * @param <Y> Type of the FunctionData
         * @return New Function.Builder Instance
         */
        @SuppressWarnings("unchecked")
        public static <X extends Function<Y>, Y extends FunctionData> Builder<X, Y> getInstance(Class<X> functionClass) {
            ParameterizedType parameterizedType = (ParameterizedType)functionClass.getGenericSuperclass();
            Type genericType = parameterizedType.getActualTypeArguments()[0]; // TODO Check Array Size / Null Pointer
            Class<Y> functionDataClass = (Class<Y>) genericType;
            return new Builder<>(functionClass, functionDataClass);
        }
        private Builder(Class<F> functionClass, Class<D> functionDataClass) {
            if(functionClass == null || functionDataClass == null) {
                throw new IllegalArgumentException("FunctionClass or FunctionDataClass are not allowed to be null for a Function.Builder");
            }
            this.functionClass = functionClass;
            this.functionDataClass = functionDataClass;
            try {
                this.constructor = functionClass.getConstructor(String.class, Class.class, getFunctionDataClass());
                this.constructor.newInstance(null, null, null); // Check if Constructor is available during initialization.
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Function [" + functionClass + "] does not provide a Constructor with " +
                        "appropriate visibility of form (String, Class, " + functionDataClass.getName() + ")", e);
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
        public Builder<F, D> functionData(D functionData) {
            this.functionData = functionData;
            return this;
        }
        protected String getFieldName() {
            return fieldName;
        }
        protected Class getFieldClass() {
            return fieldClass;
        }
        protected D getFunctionData() {
            return functionData;
        }
        public Class<F> getFunctionClass() {
            return functionClass;
        }
        public Class<D> getFunctionDataClass() {
            return functionDataClass;
        }
        public F build() {
            // Check parameters before building
            if(getFieldName() == null || getFieldName().isEmpty()) {
                throw new IllegalArgumentException("Cannot build Function, FieldName cannot be null or empty");
            }
            if(getFieldClass() == null) {
                throw new IllegalArgumentException("Cannot build Function, FieldClass cannot be null");
            }
            try {
                return constructor.newInstance(getFieldName(), getFieldClass(), getFunctionData());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // should not be thrown as tested in constructor
                throw new IllegalArgumentException("Function does not provide a Constructor with appropriate visibility of form " +
                        "(String, " + getFunctionData().getClass().getName() + ")");
            }
        }
    }

    public Function(String fieldName, Class fieldClass, D functionData) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.functionData = functionData;
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

    public D getFunctionData() {
        return functionData;
    }

    protected void setFunctionData(D functionData) {
        this.functionData = functionData;
    }

    public abstract <T> Collector<T, ?, ?> toCollector();
}
