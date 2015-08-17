package de.schuermann.interactivedata.spring.service.controllers;

import de.schuermann.interactivedata.api.data.DataRequest;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.api.chart.annotations.Axis;
import de.schuermann.interactivedata.api.chart.annotations.Chart;
import de.schuermann.interactivedata.api.chart.annotations.LineChart;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.TimeFilter;
import de.schuermann.interactivedata.api.data.operations.functions.Count;
import de.schuermann.interactivedata.api.data.operations.granularity.TimeGranularity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * @author Philipp Sch�rmann
 */
@Service
@ChartService("counter")
public class ChartController {

    @Chart("line")
    @LineChart(
            dataSource = MockDataSource.class,
            axis = {
                    @Axis(
                            dataField = "time",
                            dataType = Instant.class,
                            type = Axis.Type.X,
                            filter = TimeFilter.class,
                            granularity = TimeGranularity.class
                    ),
                    @Axis(
                            dataField = "value",
                            dataType = Integer.class,
                            type = Axis.Type.Y,
                            functions = Count.class
                    )
            }
    )
    public LineChartData lineChart(LineChartData data) {
        // Further enhance data before sending
        return data;
    }

    public static class MockDataSource implements DataSource {

        @Override
        public List<DataObject> getData(DataRequest dataRequest) {
            return null;
        }
    }
}
