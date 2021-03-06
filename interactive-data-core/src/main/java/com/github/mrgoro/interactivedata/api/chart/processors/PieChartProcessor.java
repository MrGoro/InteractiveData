package com.github.mrgoro.interactivedata.api.chart.processors;

import com.github.mrgoro.interactivedata.api.chart.annotations.pie.Field;
import com.github.mrgoro.interactivedata.api.chart.annotations.pie.PieChart;
import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.FilterInfo;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.FunctionInfo;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.OperationInfo;
import com.github.mrgoro.interactivedata.api.chart.definitions.pie.FieldDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.pie.PieChartDefinition;
import com.github.mrgoro.interactivedata.api.data.operations.filter.Filter;
import com.github.mrgoro.interactivedata.api.service.annotations.AnnotationProcessorService;
import com.github.mrgoro.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.util.Collections;

/**
 * Processor for {@link PieChart @PieChart} annotation.
 * <p>
 * Generates the {@link AbstractChartDefinition ChartDefinition} for pie charts of the type
 * {@link PieChartDefinition}
 *
 * @author Philipp Sch&uuml;rmann
 */
@AnnotationProcessorService
public class PieChartProcessor implements AnnotationProcessor<PieChart> {

    @Override
    public AbstractChartDefinition<?, ?> process(PieChart annotation) {
        if (annotation.label().length > 1) {
            throw new ChartDefinitionException("PieChart cannot have more than one label definition");
        }
        PieChartDefinition pieChartDefinition = new PieChartDefinition();

        Field fieldData = annotation.data();
        pieChartDefinition.addField(getFieldDefinition(fieldData, Field.Type.DATA));
        processFilters(pieChartDefinition, fieldData);
        if (annotation.label().length == 1) {
            Field fieldLabel = annotation.label()[0];
            pieChartDefinition.addField(getFieldDefinition(fieldLabel, Field.Type.LABEL));
            processFilters(pieChartDefinition, fieldLabel);
            processOperations(pieChartDefinition, fieldData, fieldLabel);
        }

        return pieChartDefinition;
    }

    public FieldDefinition getFieldDefinition(Field field, Field.Type type) {
        FieldDefinition axisDefinition = new FieldDefinition(type);
        axisDefinition.setDataField(field.dataField());
        if (field.functions().length == 1) {
            axisDefinition.setDataField(axisDefinition.getDataField() + "." + field.functions()[0].getSimpleName().toLowerCase());
        }
        axisDefinition.setDataType(field.dataType());
        return axisDefinition;
    }

    public void processFilters(AbstractChartDefinition<?, ?> definition, Field field) {
        for (Class<? extends Filter<?, ?>> filterClass : field.filter()) {
            FilterInfo filterInfo = new FilterInfo();
            filterInfo.setFieldName(field.dataField());
            filterInfo.setFieldClass(field.dataType());
            filterInfo.setFilter(filterClass);
            definition.addFilter(filterInfo);
        }
    }

    public void processOperations(AbstractChartDefinition<?, ?> definition, Field data, Field label) {
        if (data.functions().length > 1) {
            throw new ChartDefinitionException("PieChart can only have one function");
        }
        if (data.granularity().length > 0) {
            throw new ChartDefinitionException("PieChart cannot have a granularity on data field");
        }
        if (label.granularity().length > 1) {
            throw new ChartDefinitionException("PieChart can only have one granularity");
        }
        if (label.functions().length > 0) {
            throw new ChartDefinitionException("PieChart cannot have a function on label field");
        }
        if (data.functions().length != label.granularity().length) {
            throw new ChartDefinitionException("PieChart must have one function on data and one granularity on label");
        }
        if (data.functions().length == 1 && label.granularity().length == 1) {
            OperationInfo operationInfo = new OperationInfo();
            operationInfo.setFieldName(label.dataField());
            operationInfo.setFieldClass(label.dataType());
            operationInfo.setGranularity(label.granularity()[0]);

            FunctionInfo functionInfo = new FunctionInfo();
            functionInfo.setFieldName(data.dataField());
            functionInfo.setFieldClass(data.dataType());
            functionInfo.setFunction(data.functions()[0]);

            operationInfo.setFunctionInfos(Collections.singletonList(functionInfo));

            definition.addOperation(operationInfo);
        }
    }
}