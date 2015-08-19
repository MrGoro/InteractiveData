package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.annotations.Axis;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.AxisDefinition;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected LineChartData convertData(List<DataObject> chartData) {
        Map data = new HashMap();
        LineChartDefinition chartDefinition = getChartDefinition();
        AxisDefinition axisDefinitionX = chartDefinition.getAxis(Axis.Type.X);
        AxisDefinition axisDefinitionY = chartDefinition.getAxis(Axis.Type.Y);
        for(DataObject dataObject : chartData) {
            data.put(dataObject.getProperty(axisDefinitionX.getDataField()), dataObject.getProperty(axisDefinitionY.getDataField()));
        }
        LineChartData lineChart = new LineChartData(chartDefinition.getName(), data);
        return lineChart;
    }
}
