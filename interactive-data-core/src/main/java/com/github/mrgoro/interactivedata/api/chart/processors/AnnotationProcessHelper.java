package com.github.mrgoro.interactivedata.api.chart.processors;

import com.github.mrgoro.interactivedata.api.chart.annotations.*;
import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.ServiceDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.FilterInfo;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.FunctionInfo;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.OperationInfo;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Annotation processor providing definitions for {@link Chart @Chart} base annotation.
 * <p>
 * Provides helper methods for extracting information of {@link FilterDef @FilterDef}, {@link OperationDef @OperationDef},
 * {@link FunctionDef @FunctionDef} and {@link Option @Option[]}.
 */
public class AnnotationProcessHelper {

    private static Map<String, ServiceDefinition> serviceDefinitions = new HashMap<>();

    public static <T extends AbstractChartDefinition<?, ?>> T processChartAnnotation(T chartDefinition, Chart annotation, ChartService serviceAnnotation) {
        // Add Service Definition if not exists
        if(!serviceDefinitions.containsKey(serviceAnnotation.value())) {
            ServiceDefinition serviceDefinition = new ServiceDefinition(serviceAnnotation.value());
            serviceDefinition.setDescription(serviceAnnotation.description());
            serviceDefinitions.put(serviceAnnotation.value(), serviceDefinition);
        }

        chartDefinition.setServiceDefinition(serviceDefinitions.get(serviceAnnotation.value()));

        chartDefinition.setName(annotation.name());
        chartDefinition.setDescription(annotation.description());

        chartDefinition.setDataSource(annotation.dataSource());

        for (FilterDef filterDef : annotation.filter()) {
            chartDefinition.addFilter(getFilterInfo(filterDef));
        }

        for (OperationDef operationDef : annotation.operations()) {
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
        for (FunctionDef functionDef : functions) {
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
        for (Option option : options) {
            map.put(option.key(), option.value());
        }
        return map;
    }
}