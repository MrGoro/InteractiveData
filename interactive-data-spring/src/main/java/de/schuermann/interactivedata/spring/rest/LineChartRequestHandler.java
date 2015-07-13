package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class LineChartRequestHandler extends AbstractRequestHandler<LineChartDefinition, LineChartData> {

    public LineChartRequestHandler(ApplicationContext applicationContext, LineChartDefinition chartDefinition) {
        super(applicationContext, chartDefinition);
    }

    @Override
    protected LineChartData getData() {
        List data = getDataSource().getData(null, null, null);
        return new LineChartData(getChartDefinition().getName());
    }

}
