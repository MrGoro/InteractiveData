package de.schuermann.interactivedata.api.filter;

import java.util.Date;

/**
 * Data Trasfer Object for {@Link Filter Filter} information of {@Link TimeFilter TimeFilter}.
 *
 * @author Philipp Schürmann
 */
public class TimeFilterData implements FilterData {

    private Date start;
    private Date end;

    public TimeFilterData() {
    }

    public TimeFilterData(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
