package com.github.mrgoro.interactivedata.api.chart.definitions.line;

import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.Axis;
import com.github.mrgoro.interactivedata.api.chart.data.LineChartData;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Philipp Sch&uuml;rmann
 */
public class LineChartDefinition extends AbstractChartDefinition<AxisDefinition, LineChartData> {

    public List<AxisDefinition> getAxis(Axis.Type type) {
        return getDimensions().stream().filter(axis -> axis.getType().equals(type)).collect(toList());
    }

    public void addAxis(AxisDefinition axis) {
        addDimension(axis);
    }

}
