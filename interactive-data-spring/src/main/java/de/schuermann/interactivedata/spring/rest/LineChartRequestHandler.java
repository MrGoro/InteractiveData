package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import org.springframework.context.ApplicationContext;

/**
 * @author Philipp Schürmann
 */
public class LineChartRequestHandler extends AbstractChartRequestHandler<LineChartDefinition, LineChartData> {

    public LineChartRequestHandler(ApplicationContext applicationContext, LineChartDefinition chartDefinition) {
        super(applicationContext, chartDefinition);
    }

    @Override
    protected LineChartData convertData(ChartData chartData) {
        return new LineChartData(getChartDefinition().getName());
    }

}
