package com.github.mrgoro.interactivedata.api.handler;

import com.github.mrgoro.interactivedata.api.chart.annotations.line.Axis;
import com.github.mrgoro.interactivedata.api.chart.definitions.line.AxisDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.line.LineChartDefinition;
import com.github.mrgoro.interactivedata.api.service.DataMapperService;
import com.github.mrgoro.interactivedata.api.service.ServiceProvider;
import com.github.mrgoro.interactivedata.api.chart.data.LineChartData;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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
        AxisDefinition xAxis = chartDefinition.getAxis(Axis.Type.X).get(0);
        List<AxisDefinition> yAxisList = chartDefinition.getAxis(Axis.Type.Y);
        yAxisList.forEach(yAxis ->
            data.add(chartData.stream()
                .map(getMapper(xAxis.getDataField(), yAxis.getDataField()))
                .sorted(arrayIndexComparator)
                .collect(toList())));
        return new LineChartData(chartDefinition.getName(), data);
    }

    private static Function<DataObject, Object[]> getMapper(String x, String y) {
        return (dataObject) -> new Object[] {
                dataObject.getOptionalProperty(x).orElse(""),
                dataObject.getOptionalProperty(y).orElse("")
        };
    }

    @SuppressWarnings("unchecked")
    private static final Comparator<Object[]> arrayIndexComparator = (o1, o2) -> ((Comparable)o1[0]).compareTo(o2[0]);
}