package de.schuermann.interactivedata.api.chart.data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link de.schuermann.interactivedata.api.chart.annotations.heatmap.Heatmap Heatmap}.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class HeatmapData extends ChartData {

    private List<Point> data = new ArrayList<>();
    private Object max;

    public HeatmapData(String name) {
        super(name);
    }

    public List<Point> getData() {
        return data;
    }

    public void setData(List<Point> data) {
        this.data = data;
    }

    public void addPoint(Object x, Object y, Object value) {
        addPoint(new Point(x, y, value));
    }

    public void addPoint(Point p) {
        data.add(p);
    }

    public Object getMax() {
        return max;
    }

    public void setMax(Object max) {
        this.max = max;
    }

    public class Point {
        private Object x;
        private Object y;
        private Object value;

        public Point(Object x, Object y, Object value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public Object getX() {
            return x;
        }

        public void setX(Object x) {
            this.x = x;
        }

        public Object getY() {
            return y;
        }

        public void setY(Object y) {
            this.y = y;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
