package de.schuermann.interactivedata.api.chart.processors;

import de.schuermann.interactivedata.api.chart.annotations.line.Axis;
import de.schuermann.interactivedata.api.chart.annotations.line.LineChart;
import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.line.AxisDefinition;
import de.schuermann.interactivedata.api.chart.definitions.line.LineChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.operations.FilterInfo;
import de.schuermann.interactivedata.api.chart.definitions.operations.FunctionInfo;
import de.schuermann.interactivedata.api.chart.definitions.operations.OperationInfo;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;
import de.schuermann.interactivedata.api.service.annotations.AnnotationProcessorService;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@AnnotationProcessorService
public class LineChartProcessor implements AnnotationProcessor<LineChart> {

    @Override
    public AbstractChartDefinition<?, ? extends ChartData> process(LineChart annotation) {
        LineChartDefinition definition = new LineChartDefinition();
        if(Arrays.stream(annotation.axis()).filter(axis -> axis.type() == Axis.Type.X).count() != 1) {
            throw new ChartDefinitionException("LineChart must have exactly one X axis");
        }
        for(Axis axis : annotation.axis()) {
            if(axis.granularity().length > 1) {
                throw new ChartDefinitionException("LineChart can only have one Granularity on an axis.");
            }
            if(axis.functions().length > 1) {
                throw new ChartDefinitionException("LineChart can only have one Function on an axis.");
            }
            definition.addAxis(getAxisDefinition(axis));
            for (Class<? extends Filter<?,?>> filterClass : axis.filter()) {
                FilterInfo filterInfo = new FilterInfo();
                filterInfo.setFieldName(axis.dataField());
                filterInfo.setFieldClass(axis.dataType());
                filterInfo.setFilter(filterClass);
                definition.addFilter(filterInfo);
            }
            for (Class<? extends Granularity<?,?>> granularityClass : axis.granularity()) {
                OperationInfo operationInfo = new OperationInfo();
                operationInfo.setFieldName(axis.dataField());
                operationInfo.setFieldClass(axis.dataType());
                operationInfo.setGranularity(granularityClass);

                List<FunctionInfo> functionInfos = new ArrayList<>();
                Map<Axis, Class<? extends Function<?,?>>[]> functions = Arrays.stream(annotation.axis())
                        .filter(streamAxis -> !streamAxis.dataField().equals(axis.dataField()))
                        .filter(streamAxis -> streamAxis.functions().length > 0)
                        .collect(toMap(java.util.function.Function.identity(), Axis::functions));
                for(Map.Entry<Axis, Class<? extends Function<?,?>>[]> entry : functions.entrySet()) {
                    Axis functionAxis = entry.getKey();
                    for(Class<? extends Function<?,?>> function : entry.getValue()) {
                        FunctionInfo functionInfo = new FunctionInfo();
                        functionInfo.setFieldName(functionAxis.dataField());
                        functionInfo.setTargetFieldName(functionAxis.dataField());
                        functionInfo.setFieldClass(functionAxis.dataType());
                        functionInfo.setFunction(function);
                        functionInfos.add(functionInfo);
                    }
                }

                operationInfo.setFunctionInfos(functionInfos);
                definition.addOperation(operationInfo);
            }
        }
        return definition;
    }

    public AxisDefinition getAxisDefinition(Axis axis) {
        AxisDefinition axisDefinition = new AxisDefinition(axis.type());
        axisDefinition.setDataField(axis.dataField());
        if(axis.functions().length == 1) {
            axisDefinition.setDataField(axisDefinition.getDataField()+"."+axis.functions()[0].getSimpleName().toLowerCase());
        }
        axisDefinition.setDataType(axis.dataType());
        return axisDefinition;
    }
}