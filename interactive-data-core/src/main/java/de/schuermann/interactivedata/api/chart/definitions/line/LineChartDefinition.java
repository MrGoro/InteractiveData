package de.schuermann.interactivedata.api.chart.definitions.line;

import de.schuermann.interactivedata.api.chart.annotations.line.Axis;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Philipp Schürmann
 */
public class LineChartDefinition extends AbstractChartDefinition<AxisDefinition, LineChartData> {

    public List<AxisDefinition> getAxis(Axis.Type type) {
        return getDimensions().stream().filter(axis -> axis.getType().equals(type)).collect(toList());
    }

    public void addAxis(AxisDefinition axis) {
        addDimension(axis);
    }

}
