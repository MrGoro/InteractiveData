package de.schuermann.interactivedata.spring.sample.chartapi;

import de.schuermann.interactivedata.api.data.operations.functions.Average;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.api.chart.annotations.Axis;
import de.schuermann.interactivedata.api.chart.annotations.Chart;
import de.schuermann.interactivedata.api.chart.annotations.LineChart;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.data.operations.filter.TimeFilter;
import de.schuermann.interactivedata.api.data.operations.functions.Count;
import de.schuermann.interactivedata.api.data.operations.granularity.TimeGranularity;
import de.schuermann.interactivedata.spring.sample.datasource.ActionDataSource;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Demo to build an REST API for the visualization of a counter using the Interactive Data library.
 *
 * @author Philipp Schürmann
 */
@Service
@ChartService("counter")
public class CounterVisualization {

    @Chart("line")
    @LineChart(
        dataSource = ActionDataSource.class,
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
            ),
            @Axis(
                dataField = "id",
                dataType = Long.class,
                type = Axis.Type.Y,
                functions = Average.class
            )
        }
    )
    public LineChartData lineChart(LineChartData data) {
        // Further enhance data before sending
        return data;
    }

    public void testMethodWithoutAnnotation() {
        // do nothing
    }
}