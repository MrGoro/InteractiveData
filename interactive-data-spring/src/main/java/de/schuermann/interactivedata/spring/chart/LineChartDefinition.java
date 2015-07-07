package de.schuermann.interactivedata.spring.chart;

import de.schuermann.interactivedata.api.chart.types.line.Axis;
import de.schuermann.interactivedata.api.data.DataSource;

/**
 * @author Philipp Schürmann
 */
public class LineChartDefinition extends AbstractChartDefinition<AxisDefinition> {

    public LineChartDefinition(String name, DataSource dataSource) {
        super(name, dataSource);
    }

    public AxisDefinition getAxis(Axis.Type type) {
        for(AxisDefinition axis : getDimensions()) {
            if(axis.getType() == type) {
                return axis;
            }
        }
        return null;
    }

    public void setAxis(AxisDefinition axis) {
        addDimension(axis);
    }

}
