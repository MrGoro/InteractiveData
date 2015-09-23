package com.github.mrgoro.interactivedata.api.chart.definitions.pie;

import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.annotations.pie.Field;
import com.github.mrgoro.interactivedata.api.chart.data.PieChartData;

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
