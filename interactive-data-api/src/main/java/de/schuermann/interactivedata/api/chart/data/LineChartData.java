package de.schuermann.interactivedata.api.chart.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class LineChartData extends ChartData {

    private Map<String, String> data = new HashMap<>();

    public LineChartData(String name) {
        super(name);
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
