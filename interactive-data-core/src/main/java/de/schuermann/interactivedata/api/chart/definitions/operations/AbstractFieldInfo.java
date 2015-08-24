package de.schuermann.interactivedata.api.chart.definitions.operations;

import java.util.Map;

public class AbstractFieldInfo {
    private String fieldName;
    private Class<?> fieldClass;
    private Map<String, String> options;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class<?> fieldClass) {
        this.fieldClass = fieldClass;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}