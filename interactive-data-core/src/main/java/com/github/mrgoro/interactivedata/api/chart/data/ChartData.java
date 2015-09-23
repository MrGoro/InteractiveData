package com.github.mrgoro.interactivedata.api.chart.data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base Data Transfer Object for data of charts. Used by the api - after serialization.
 * <p>
 * Custom DTOs of each chart type must extent this data type.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class ChartData implements Serializable {

    private String name;
    private LocalDateTime time = LocalDateTime.now();

    public ChartData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    protected void setTime(LocalDateTime time) {
        this.time = time;
    }
}
