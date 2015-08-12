package de.schuermann.interactivedata.api.chart.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class LineChartData extends ChartData {

    Map data = new HashMap<>();

    public LineChartData(String name) {
        super(name);
    }

    public LineChartData(String name, Map data) {
        super(name);
        this.data = data;
    }

    public Map getData() {
        return data;
    }

}
