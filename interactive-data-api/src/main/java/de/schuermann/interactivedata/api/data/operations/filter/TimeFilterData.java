package de.schuermann.interactivedata.api.data.operations.filter;

import java.time.Instant;

/**
 * Data Trasfer Object for {@Link Filter Filter} information of {@Link TimeFilter TimeFilter}.
 *
 * @author Philipp Schürmann
 */
public class TimeFilterData implements FilterData {

    private Instant start;
    private Instant end;

    public TimeFilterData() {
    }

    public TimeFilterData(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "TimeFilterData{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
