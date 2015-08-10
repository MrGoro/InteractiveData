package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Philipp Schürmann
 */
@ChartRequestHandlerService
@Named
public class LineChartRequestHandler extends ChartRequestHandler<LineChartDefinition, LineChartData> {

    @Inject
    public LineChartRequestHandler(DataMapperService dataMapperService, ServiceProvider serviceProvider) {
        super(dataMapperService, serviceProvider);
    }

    @Override
    protected LineChartData convertData(ChartData chartData) {
        LineChartData lineChart = new LineChartData(chartData.getName());
        chartData.getVariates().forEach(lineChart::addVariate);
        return lineChart;
    }
}
