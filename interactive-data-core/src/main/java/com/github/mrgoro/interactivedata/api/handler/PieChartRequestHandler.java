package com.github.mrgoro.interactivedata.api.handler;

import com.github.mrgoro.interactivedata.api.chart.annotations.pie.Field;
import com.github.mrgoro.interactivedata.api.chart.data.PieChartData;
import com.github.mrgoro.interactivedata.api.chart.definitions.pie.FieldDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.pie.PieChartDefinition;
import com.github.mrgoro.interactivedata.api.service.DataMapperService;
import com.github.mrgoro.interactivedata.api.service.ServiceProvider;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Request Handler for visual mapping of pie charts
 *
 * @author Philipp Sch&uuml;rmann
 */
@ChartRequestHandlerService
@Named
public class PieChartRequestHandler extends ChartRequestHandler<PieChartDefinition, PieChartData> {

    @Inject
    public PieChartRequestHandler(DataMapperService dataMapperService, ServiceProvider serviceProvider) {
        super(dataMapperService, serviceProvider);
    }

    @Override
    protected PieChartData convertData(List<DataObject> chartData) {
        long detailsCount = 4;
        FieldDefinition fieldData = getChartDefinition().getField(Field.Type.DATA).get();
        FieldDefinition fieldLabel = getChartDefinition().getField(Field.Type.LABEL).orElse(fieldData);
        List<Object[]> data = chartData.stream()
                .sorted(Comparator.comparing(
                    dataObject -> dataObject.getOptionalProperty(fieldData.getDataField(), Long.class).orElse(0L)
                ))
                .map(dataObject ->
                    new Object[]{
                        dataObject.getOptionalProperty(fieldData.getDataField()).orElse("0"),
                        dataObject.getOptionalProperty(fieldLabel.getDataField()).orElse("No Data")
                    }
                )
                .collect(toList());

        // Group rest
        if(data.size() > detailsCount+1) {
            long rest = data.stream().skip(detailsCount).mapToLong(
                array -> (Long) array[0]
            ).sum();
            data = data.stream().limit(detailsCount).collect(toList());
            data.add(
                new Object[]{rest, "Rest"}
            );
        }

        return new PieChartData(getChartDefinition().getName(), data);
    }
}