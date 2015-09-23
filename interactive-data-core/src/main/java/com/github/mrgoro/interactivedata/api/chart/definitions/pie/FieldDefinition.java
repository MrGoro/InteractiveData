package com.github.mrgoro.interactivedata.api.chart.definitions.pie;

import com.github.mrgoro.interactivedata.api.chart.annotations.pie.Field;
import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractDimension;

/**
 * @author Philipp Sch&uuml;rmann
 */
public class FieldDefinition extends AbstractDimension {

    private Field.Type type;

    public FieldDefinition(Field.Type type) {
        this.type = type;
    }

    public Field.Type getType() {
        return type;
    }

    public void setType(Field.Type type) {
        this.type = type;
    }
}
