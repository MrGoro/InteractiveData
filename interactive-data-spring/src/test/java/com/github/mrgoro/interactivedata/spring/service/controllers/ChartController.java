package com.github.mrgoro.interactivedata.spring.service.controllers;

import com.github.mrgoro.interactivedata.api.chart.annotations.Chart;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.Axis;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.LineChart;
import com.github.mrgoro.interactivedata.api.chart.data.LineChartData;
import com.github.mrgoro.interactivedata.api.data.DataRequest;
import com.github.mrgoro.interactivedata.api.data.source.DataSource;
import com.github.mrgoro.interactivedata.api.data.operations.filter.TimeFilter;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Count;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.TimeGranularity;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Service
@ChartService("counter")
public class ChartController {

    @Chart(
        name = "line",
        dataSource = MockDataSource.class
    )
    @LineChart(
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
