package de.schuermann.interactivedata.api.chart.definitions.operations;

import de.schuermann.interactivedata.api.data.operations.functions.Function;

public class FunctionInfo extends AbstractFieldInfo {
    private Class<? extends Function<?, ?>> function;
    private String targetFieldName;

    public Class<? extends Function<?, ?>> getFunction() {
        return function;
    }

    public void setFunction(Class<? extends Function<?, ?>> function) {
        this.function = function;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }
}