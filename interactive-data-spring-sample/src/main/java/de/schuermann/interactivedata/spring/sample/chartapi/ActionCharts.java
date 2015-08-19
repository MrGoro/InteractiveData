package de.schuermann.interactivedata.spring.sample.chartapi;

import de.schuermann.interactivedata.api.chart.annotations.*;
import de.schuermann.interactivedata.api.chart.data.LineChartData;
import de.schuermann.interactivedata.api.data.operations.filter.TimeFilter;
import de.schuermann.interactivedata.api.data.operations.functions.Concatenation;
import de.schuermann.interactivedata.api.data.operations.functions.Count;
import de.schuermann.interactivedata.api.data.operations.granularity.TimeGranularity;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.spring.sample.datasource.ActionDataSource;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

/**
 * Demo for an api that uses an database source and offers a line chart with two Y axis.
 *
 * Demonstrates two different types of chart definitions with the same result.
 * The first one is easier to define. The second offers more flexibility.
 *
 * @author Philipp Schürmann
 */
@Service
@ChartService("db")
public class ActionCharts {

    @Chart(
        value = "actions1",
        dataSource = ActionDataSource.class
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
                dataField = "id",
                dataType = Long.class,
                type = Axis.Type.Y,
                functions = Count.class
            ),
            @Axis(
                dataField = "id",
                dataType = Long.class,
                type = Axis.Type.Y,
                functions = Concatenation.class
            )
        }
    )
    public LineChartData actions1(LineChartData data) {
        // Further enhance data before sending
        return data;
    }

    @Chart(
        value = "actions2",
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
                        function = Concatenation.class
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
                dataField = "id.concatenation",
                dataType = Long.class,
                type = Axis.Type.Y
            )
        }
    )
    public LineChartData actions2(LineChartData data) {
        // Further enhance data before sending
        return data;
    }
}