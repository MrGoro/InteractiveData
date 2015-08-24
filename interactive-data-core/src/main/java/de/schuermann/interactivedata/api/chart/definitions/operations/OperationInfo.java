package de.schuermann.interactivedata.api.chart.definitions.operations;

import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;

import java.util.List;

public class OperationInfo extends AbstractFieldInfo {
    private Class<? extends Granularity<?, ?>> granularity;
    private List<FunctionInfo> functionInfos;

    public Class<? extends Granularity<?, ?>> getGranularity() {
        return granularity;
    }

    public void setGranularity(Class<? extends Granularity<?, ?>> granularity) {
        this.granularity = granularity;
    }

    public List<FunctionInfo> getFunctionInfos() {
        return functionInfos;
    }

    public void setFunctionInfos(List<FunctionInfo> functionInfos) {
        this.functionInfos = functionInfos;
    }
}