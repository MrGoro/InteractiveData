package de.schuermann.interactivedata.api.chart.definitions.line;

import de.schuermann.interactivedata.api.chart.annotations.line.Axis;
import de.schuermann.interactivedata.api.chart.definitions.AbstractDimension;

/**
 * @author Philipp Schürmann
 */
public class AxisDefinition extends AbstractDimension {

    private Axis.Type type;

    public AxisDefinition(Axis.Type type) {
        this.type = type;
    }

    public Axis.Type getType() {
        return type;
    }

    public void setType(Axis.Type type) {
        this.type = type;
    }
}
