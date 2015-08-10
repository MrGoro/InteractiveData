package de.schuermann.interactivedata.api.chart.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class ChartData implements Serializable {

    private String name;
    private LocalDateTime time = LocalDateTime.now();

    private List<Variate> variates = new ArrayList<>();

    public ChartData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    protected void setTime(LocalDateTime time) {
        this.time = time;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public void addVariate(Variate variate) {
        variates.add(variate);
    }

    public List<Variate> getVariates() {
        return variates;
    }
}
