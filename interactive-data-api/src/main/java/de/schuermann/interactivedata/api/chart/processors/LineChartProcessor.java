package de.schuermann.interactivedata.api.chart.processors;

import de.schuermann.interactivedata.api.chart.annotations.Axis;
import de.schuermann.interactivedata.api.chart.annotations.LineChart;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.AxisDefinition;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;

import java.util.Arrays;

public class LineChartProcessor implements AnnotationProcessor<LineChart> {

    @Override
    public AbstractChartDefinition process(String name, LineChart annotation) {
        LineChartDefinition definition = new LineChartDefinition(name, annotation.dataSource());

        for(Axis axis : annotation.axis()) {
            definition.addAxis(getAxisDefinition(axis));
        }

        return definition;
    }

    public AxisDefinition getAxisDefinition(Axis axis) {
        AxisDefinition axisDefinition = new AxisDefinition(axis.type());
        axisDefinition.setDataField(axis.dataField());
        axisDefinition.setDataType(axis.dataType());
        axisDefinition.setFilters(Arrays.asList(axis.filter()));
        axisDefinition.setGranularities(Arrays.asList(axis.granularity()));
        axisDefinition.setFunctions(Arrays.asList(axis.functions()));
        return axisDefinition;
    }
}