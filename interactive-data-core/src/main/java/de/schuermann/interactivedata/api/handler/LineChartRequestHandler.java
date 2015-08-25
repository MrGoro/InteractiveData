package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.annotations.line.Axis;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.line.AxisDefinition;
import de.schuermann.interactivedata.api.chart.definitions.line.LineChartDefinition;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author Philipp Sch&uuml;rmann
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
        List<List<Object[]>> data = new ArrayList<>();
        LineChartDefinition chartDefinition = getChartDefinition();
        AxisDefinition axisDefinitionX = chartDefinition.getAxis(Axis.Type.X).get(0);
        List<AxisDefinition> axisDefinitionsY = chartDefinition.getAxis(Axis.Type.Y);
        for(AxisDefinition axisDefinitionY : axisDefinitionsY) {
            data.add(chartData.stream()
                    .map(dataObject -> new Object[]{
                            dataObject.getProperty(axisDefinitionX.getDataField()),
                            dataObject.getProperty(axisDefinitionY.getDataField())
                    })
                    .sorted(arrayIndexComparator)
                    .collect(toList()));
        }
        return new LineChartData(chartDefinition.getName(), data);
    }

    private Comparator<Object[]> arrayIndexComparator = (objects, otherobjects) -> ((Comparable)objects[0]).compareTo(otherobjects[0]);
}