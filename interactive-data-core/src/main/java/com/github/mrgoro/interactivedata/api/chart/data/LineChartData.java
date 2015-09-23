package com.github.mrgoro.interactivedata.api.chart.data;

import com.github.mrgoro.interactivedata.api.chart.annotations.line.LineChart;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link LineChart LineChart}.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class LineChartData extends ChartData {

    List<List<Object[]>> data = new ArrayList<>();

    public LineChartData(String name) {
        super(name);
    }

    public LineChartData(String name, List<List<Object[]>> data) {
        super(name);
        this.data = data;
    }

    public List<List<Object[]>> getData() {
        return data;
    }

}
