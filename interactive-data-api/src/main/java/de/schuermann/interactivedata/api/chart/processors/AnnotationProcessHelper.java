package de.schuermann.interactivedata.api.chart.processors;

import de.schuermann.interactivedata.api.chart.annotations.*;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.operations.FilterInfo;
import de.schuermann.interactivedata.api.chart.definitions.operations.FunctionInfo;
import de.schuermann.interactivedata.api.chart.definitions.operations.OperationInfo;
import de.schuermann.interactivedata.api.service.annotations.ChartService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationProcessHelper {

    public static <T extends AbstractChartDefinition<?, ?>> T processChartAnnotation(T chartDefinition, Chart annotation, ChartService serviceAnnotation) {
        chartDefinition.setName(serviceAnnotation.value() + "/" + annotation.value());
        chartDefinition.setDataSource(annotation.dataSource());

        for(FilterDef filterDef : annotation.filter()) {
            chartDefinition.addFilter(getFilterInfo(filterDef));
        }

        for(OperationDef operationDef : annotation.operations()) {
            chartDefinition.addOperation(getOperationInfo(operationDef));
        }

        return chartDefinition;
    }

    public static FilterInfo getFilterInfo(FilterDef filterDef) {
        FilterInfo filterInfo = new FilterInfo();
        filterInfo.setFieldName(filterDef.fieldName());
        filterInfo.setFieldClass(filterDef.fieldClass());
        filterInfo.setFilter(filterDef.filter());
        filterInfo.setOptions(getOptions(filterDef.options()));
        return filterInfo;
    }

    public static OperationInfo getOperationInfo(OperationDef operationDef) {
        OperationInfo operationInfo = new OperationInfo();
        operationInfo.setFieldName(operationDef.fieldName());
        operationInfo.setFieldClass(operationDef.fieldClass());
        operationInfo.setGranularity(operationDef.granularity());
        operationInfo.setOptions(getOptions(operationDef.options()));

        operationInfo.setFunctionInfos(getFunctionInfos(operationDef.functions()));

        return operationInfo;
    }

    private static List<FunctionInfo> getFunctionInfos(FunctionDef[] functions) {
        List<FunctionInfo> functionInfos = new ArrayList<>();
        for(FunctionDef functionDef : functions) {
            functionInfos.add(getFunctionInfo(functionDef));
        }
        return functionInfos;
    }

    private static FunctionInfo getFunctionInfo(FunctionDef functionDef) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setFieldName(functionDef.fieldName());
        functionInfo.setFieldClass(functionDef.fieldClass());
        functionInfo.setFunction(functionDef.function());
        functionInfo.setOptions(getOptions(functionDef.options()));
        return functionInfo;
    }

    public static Map<String, String> getOptions(Option[] options) {
        Map<String, String> map = new HashMap<>();
        for(Option option : options) {
            map.put(option.key(), option.value());
        }
        return map;
    }
}