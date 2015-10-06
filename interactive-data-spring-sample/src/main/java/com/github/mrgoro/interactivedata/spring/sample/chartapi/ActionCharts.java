package com.github.mrgoro.interactivedata.spring.sample.chartapi;

import com.github.mrgoro.interactivedata.api.chart.annotations.Chart;
import com.github.mrgoro.interactivedata.api.chart.annotations.FilterDef;
import com.github.mrgoro.interactivedata.api.chart.annotations.FunctionDef;
import com.github.mrgoro.interactivedata.api.chart.annotations.OperationDef;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.Axis;
import com.github.mrgoro.interactivedata.api.chart.annotations.line.LineChart;
import com.github.mrgoro.interactivedata.api.chart.annotations.pie.Field;
import com.github.mrgoro.interactivedata.api.chart.annotations.pie.PieChart;
import com.github.mrgoro.interactivedata.api.chart.data.LineChartData;
import com.github.mrgoro.interactivedata.api.chart.data.PieChartData;
import com.github.mrgoro.interactivedata.api.data.operations.filter.TimeFilter;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Average;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Count;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.DistinctGranularity;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.TimeGranularity;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;
import com.github.mrgoro.interactivedata.spring.sample.data.Person;
import com.github.mrgoro.interactivedata.spring.sample.datasource.ActionDataSource;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Demo for an api that uses an database source and offers two line and to pie charts from the {@link ActionDataSource}.
 *
 * Demonstrates two different types of chart definitions with the same result.
 * The first one is easier to define. The second offers more flexibility.
 *
 * @author Philipp Sch&uuml;rmann
 */
@Service
@ChartService("db")
public class ActionCharts {

    @Chart(
        name = "actionsline1",
        dataSource = ActionDataSource.class
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
                dataField = "id",
                dataType = Long.class,
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
    public LineChartData actions1(LineChartData data) {
        // Further enhance data before sending
        return data;
    }

    @Chart(
        name = "actionsline2",
        dataSource = ActionDataSource.class,
        filter = {
            @FilterDef(fieldName = "time", fieldClass = Date.class, filter = TimeFilter.class)
        },
        operations = {
            @OperationDef(
                fieldName = "time",
                fieldClass = Date.class,
                granularity = TimeGranularity.class,
                functions = {
                    @FunctionDef(
                        fieldName = "id",
                        fieldClass = Long.class,
                        function = Count.class
                    ),
                    @FunctionDef(
                        fieldName = "id",
                        fieldClass = Long.class,
                        function = Average.class
                    )
                }
            )
        }
    )
    @LineChart(
        axis = {
            @Axis(
                dataField = "time",
                dataType = Instant.class,
                type = Axis.Type.X
            ),
            @Axis(
                dataField = "id.count",
                dataType = Long.class,
                type = Axis.Type.Y
            ),
            @Axis(
                dataField = "id.average",
                dataType = Long.class,
                type = Axis.Type.Y
            )
        }
    )
    public LineChartData actions2(LineChartData data) throws InterruptedException {
        // Delay based on Data Count
        long count = data.getData().stream().mapToInt(List::size).sum();
        Thread.sleep(count);
        return data;
    }

    @Chart(
        name = "actionspie1",
        dataSource = ActionDataSource.class,
        filter = {
            @FilterDef(fieldName = "time", fieldClass = Date.class, filter = TimeFilter.class)
        },
        operations = {
            @OperationDef(
                fieldName = "person",
                fieldClass = Person.class,
                granularity = DistinctGranularity.class,
                functions = {
                    @FunctionDef(
                            fieldName = "id",
                            fieldClass = Long.class,
                            function = Count.class
                    )
                }
            )
        }
    )
    @PieChart(
        data = @Field(
            dataField = "id.count",
            dataType = Long.class
        ),
        label = @Field(
            dataField = "person",
            dataType = Person.class
        )
    )
    public PieChartData actionsPie1(PieChartData data) {
        // Further enhance data before sending
        return data;
    }

    @Chart(
            name = "actionspie2",
            dataSource = ActionDataSource.class,
            filter = {
                @FilterDef(fieldName = "time", fieldClass = Date.class, filter = TimeFilter.class)
            }
    )
    @PieChart(
        data = @Field(
            dataField = "id",
            dataType = Long.class,
            functions = Count.class
        ),
        label = @Field(
            dataField = "person",
            dataType = Person.class,
            granularity = DistinctGranularity.class
        )
    )
    public PieChartData actionsPie2(PieChartData data) {
        // Further enhance data before sending
        return data;
    }
}