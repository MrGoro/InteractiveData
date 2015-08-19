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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

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
        List<Map> data = new ArrayList<>();
        LineChartDefinition chartDefinition = getChartDefinition();
        AxisDefinition axisDefinitionX = chartDefinition.getAxis(Axis.Type.X).get(0);
        List<AxisDefinition> axisDefinitionsY = chartDefinition.getAxis(Axis.Type.Y);
        for(AxisDefinition axisDefinitionY : axisDefinitionsY) {
            data.add(chartData.stream().collect(toMap(
                    dataObject -> dataObject.getProperty(axisDefinitionX.getDataField()),
                    dataObject -> dataObject.getProperty(axisDefinitionY.getDataField())
            )));
        }
        return new LineChartData(chartDefinition.getName(), data);
    }
}
