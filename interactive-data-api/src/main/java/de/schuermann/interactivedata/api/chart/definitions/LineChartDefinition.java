package de.schuermann.interactivedata.api.chart.definitions;

import de.schuermann.interactivedata.api.chart.annotations.Axis;
import de.schuermann.interactivedata.api.data.DataSource;

/**
 * @author Philipp Schürmann
 */
public class LineChartDefinition extends AbstractChartDefinition<AxisDefinition> {

    public LineChartDefinition(String name, Class<? extends DataSource> dataSource) {
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

    public void addAxis(AxisDefinition axis) {
        addDimension(axis);
    }

}
