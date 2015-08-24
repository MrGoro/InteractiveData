package de.schuermann.interactivedata.api.chart.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link de.schuermann.interactivedata.api.chart.annotations.line.LineChart LineChart}.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class LineChartData extends ChartData {

    List<Map> data = new ArrayList<>();

    public LineChartData(String name) {
        super(name);
    }

    public LineChartData(String name, List<Map> data) {
        super(name);
        this.data = data;
    }

    public List<Map> getData() {
        return data;
    }

}
