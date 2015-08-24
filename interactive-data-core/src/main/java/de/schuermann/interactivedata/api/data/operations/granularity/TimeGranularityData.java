package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.operations.RequestData;

/**
 * @author Philipp Schürmann
 */
public class TimeGranularityData implements RequestData {

    @Override
    public boolean hasData() {
        return getSelected() != null;
    }

    public enum STEP {
        MILLISECOND,
        SECOND,
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

    private STEP selected;

    public TimeGranularityData() {
    }

    public TimeGranularityData(STEP selected) {
        this.selected = selected;
    }

    public STEP getSelected() {
        return selected;
    }

    public void setSelected(STEP selected) {
        this.selected = selected;
    }
}
