package com.github.mrgoro.interactivedata.api.chart.definitions.line;

import com.github.mrgoro.interactivedata.api.chart.annotations.line.Axis;
import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractDimension;

/**
 * @author Philipp Sch&uuml;rmann
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
