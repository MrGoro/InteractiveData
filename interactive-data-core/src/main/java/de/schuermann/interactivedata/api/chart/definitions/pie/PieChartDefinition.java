package de.schuermann.interactivedata.api.chart.definitions.pie;

import de.schuermann.interactivedata.api.chart.annotations.pie.Field;
import de.schuermann.interactivedata.api.chart.data.PieChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;

import java.util.Optional;

/**
 * @author Philipp Sch&uuml;rmann
 */
public class PieChartDefinition extends AbstractChartDefinition<FieldDefinition, PieChartData> {

    public Optional<FieldDefinition> getField(Field.Type type) {
        return getDimensions().stream().filter(field -> field.getType().equals(type)).findFirst();
    }

    public void addField(FieldDefinition fieldDefinition) {
        addDimension(fieldDefinition);
    }

}
