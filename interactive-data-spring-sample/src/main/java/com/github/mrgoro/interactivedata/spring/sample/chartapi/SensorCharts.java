package com.github.mrgoro.interactivedata.spring.sample.chartapi;

import com.github.mrgoro.interactivedata.api.chart.annotations.Chart;
import com.github.mrgoro.interactivedata.api.chart.annotations.FilterDef;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.Axis;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.LineChart;
import com.github.mrgoro.interactivedata.api.chart.data.LineChartData;
import com.github.mrgoro.interactivedata.api.data.operations.filter.SearchFilter;
import com.github.mrgoro.interactivedata.api.data.operations.filter.TimeFilter;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Average;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.TimeGranularity;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;
import com.github.mrgoro.interactivedata.spring.sample.datasource.TemperatureDataSource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Sample Visualization for temperature and humidity of Hamburg and München since 1949.
 *
 * @author Philipp Schürmann
 */
@Service
@ChartService("sensor")
public class SensorCharts {

    @Chart(
        name = "temperature",
        dataSource = TemperatureDataSource.class,
        filter = @FilterDef(
            filter = SearchFilter.class,
            fieldName = "stationId",
            fieldClass = Long.class
        )
    )
    @LineChart(
        axis = {
            @Axis(
                dataField = "time",
                dataType = Date.class,
                type = Axis.Type.X,
                filter = TimeFilter.class,
                granularity = TimeGranularity.class
            ),
            @Axis(
                dataField = "temperature",
                dataType = Double.class,
                type = Axis.Type.Y,
                functions = Average.class
            )
        }
    )
    public LineChartData temperature(LineChartData data) {
        // Further enhance data before sending
        return data;
    }
}
