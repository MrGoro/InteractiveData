package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.chart.annotations.pie.Field;
import de.schuermann.interactivedata.api.chart.data.PieChartData;
import de.schuermann.interactivedata.api.chart.definitions.pie.FieldDefinition;
import de.schuermann.interactivedata.api.chart.definitions.pie.PieChartDefinition;
import de.schuermann.interactivedata.api.data.bean.DataObject;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.service.annotations.ChartRequestHandlerService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
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
        FieldDefinition fieldData = getChartDefinition().getField(Field.Type.DATA).get();
        FieldDefinition fieldLabel = getChartDefinition().getField(Field.Type.LABEL).orElse(fieldData);
        List<Object[]> data = chartData.stream().map(dataObject ->
            new Object[]{
                dataObject.getOptionalProperty(fieldData.getDataField()).orElse("0"),
                dataObject.getOptionalProperty(fieldLabel.getDataField()).orElse("No Data")
            }
        ).collect(toList());
        return new PieChartData(getChartDefinition().getName(), data);
    }
}