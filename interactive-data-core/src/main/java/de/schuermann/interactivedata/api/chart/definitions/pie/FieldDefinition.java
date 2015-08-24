package de.schuermann.interactivedata.api.chart.definitions.pie;

import de.schuermann.interactivedata.api.chart.annotations.pie.Field;
import de.schuermann.interactivedata.api.chart.definitions.AbstractDimension;

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
