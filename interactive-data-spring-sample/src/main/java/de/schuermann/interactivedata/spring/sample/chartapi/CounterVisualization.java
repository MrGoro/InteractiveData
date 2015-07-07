package de.schuermann.interactivedata.spring.sample.chartapi;

import de.schuermann.interactivedata.api.ChartApi;
import de.schuermann.interactivedata.api.chart.types.Chart;
import de.schuermann.interactivedata.api.chart.types.line.Axis;
import de.schuermann.interactivedata.api.chart.types.line.LineChart;
import de.schuermann.interactivedata.api.chart.types.line.LineChartData;
import de.schuermann.interactivedata.api.filter.TimeFilter;
import de.schuermann.interactivedata.api.functions.Average;
import de.schuermann.interactivedata.api.granularity.TimeGranularity;
import de.schuermann.interactivedata.spring.data.RepositoryDataSource;

import java.util.Date;

/**
 * Demo to build an REST API for the visualization of a counter using the Interactive Data library.
 *
 * @author Philipp Schürmann
 */
@ChartApi("counter")
public class CounterVisualization {

    @Chart("line")
    @LineChart(
        dataSource = RepositoryDataSource.class,
        axis = {
            @Axis(
                dataField = "date",
                dataType = Date.class,
                type = Axis.Type.X,
                filter = TimeFilter.class,
                granularity = TimeGranularity.class
            ),
            @Axis(
                dataField = "value",
                dataType = Integer.class,
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