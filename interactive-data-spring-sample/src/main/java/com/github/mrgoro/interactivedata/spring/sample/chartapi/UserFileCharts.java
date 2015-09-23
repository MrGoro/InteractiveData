package com.github.mrgoro.interactivedata.spring.sample.chartapi;

import com.github.mrgoro.interactivedata.api.chart.annotations.Chart;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.Axis;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.LineChart;
import com.github.mrgoro.interactivedata.api.chart.data.LineChartData;
import com.github.mrgoro.interactivedata.api.data.operations.filter.RegexFilter;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Count;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.DistinctGranularity;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;
import com.github.mrgoro.interactivedata.spring.sample.datasource.UserCsvDataSource;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Demo for an api that uses an csv file source and offers a line chart from the {@link UserCsvDataSource}.
 *
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
