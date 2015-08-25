package de.schuermann.interactivedata.api.chart.data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link de.schuermann.interactivedata.api.chart.annotations.pie.PieChart PieChart}.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class PieChartData extends ChartData {

    List<Object[]> data = new ArrayList<>();

    public PieChartData(String name) {
        super(name);
    }

    public PieChartData(String name, List<Object[]> data) {
        super(name);
        this.data = data;
    }

    public List<Object[]> getData() {
        return data;
    }

    public static class Pair {
        private Object data;
        private Object label;

        public Pair(Object data, Object label) {
            this.data = data;
            this.label = label;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Object getLabel() {
            return label;
        }

        public void setLabel(Object label) {
            this.label = label;
        }
    }

}
