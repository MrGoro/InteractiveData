package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.RequestData;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for {@Link Filter Filter} information of {@Link TimeFilter TimeFilter}.
 *
 * @author Philipp Schürmann
 */
public class TimeFilterData implements RequestData {

    private LocalDateTime start;
    private LocalDateTime end;

    public TimeFilterData() {
    }

    public TimeFilterData(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "TimeFilterData{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean hasData() {
        return getEnd() != null && getStart() != null;
    }
}
