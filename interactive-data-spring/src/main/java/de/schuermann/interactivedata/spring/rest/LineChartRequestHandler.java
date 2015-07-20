package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class LineChartRequestHandler extends AbstractRequestHandler<LineChartDefinition, LineChartData> {

    public LineChartRequestHandler(ApplicationContext applicationContext, LineChartDefinition chartDefinition) {
        super(applicationContext, chartDefinition);
    }

    @Override
    protected LineChartData convertData(ChartData chartData) {
        return new LineChartData(getChartDefinition().getName());
    }

}
