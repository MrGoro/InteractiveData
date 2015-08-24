package de.schuermann.interactivedata.spring.sample.chartapi;

import de.schuermann.interactivedata.api.chart.annotations.Chart;
import de.schuermann.interactivedata.api.chart.annotations.line.Axis;
import de.schuermann.interactivedata.api.chart.annotations.line.LineChart;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.data.operations.filter.RegexFilter;
import de.schuermann.interactivedata.api.data.operations.functions.Count;
import de.schuermann.interactivedata.api.data.operations.granularity.DistinctGranularity;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.spring.sample.datasource.UserCsvDataSource;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Service
@ChartService("file")
public class UserFileCharts {

    @Chart(
        name = "lastnamecount",
        dataSource = UserCsvDataSource.class
    )
    @LineChart(
        axis = {
            @Axis(
                dataField = "first_name",
                dataType = String.class,
                type = Axis.Type.X,
                filter = RegexFilter.class,
                granularity = DistinctGranularity.class
            ),
            @Axis(
                dataField = "last_name",
                dataType = Instant.class,
                type = Axis.Type.Y,
                functions = Count.class
            )
        }
    )
    public LineChartData countLastnamesToFirstnames(LineChartData data) {
        // Further enhance data before sending
        return data;
    }

    public void testMethodWithoutAnnotation() {
        // do nothing
    }
}
