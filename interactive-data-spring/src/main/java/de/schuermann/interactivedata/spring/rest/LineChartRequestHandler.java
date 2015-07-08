package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;

/**
 * @author Philipp Schürmann
 */
public class LineChartRequestHandler extends AbstractRequestHandler<LineChartDefinition, LineChartData> {

    public LineChartRequestHandler(LineChartDefinition chartDefinition) {
        super(chartDefinition);
    }

    @Override
    protected LineChartData getData() {
        return new LineChartData(getChartDefinition().getName());
    }
}
