package de.schuermann.interactivedata.api.data.operations.granularity;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class TimeGranularityData implements GranularityData {

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
    private List<TemporalField> temporalFields = new ArrayList<>();

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
        createTemporalFields();
    }

    public List<TemporalField> getTemporalFields() {
        return temporalFields;
    }

    private void createTemporalFields() {
        switch (selected) {
            case MILLISECOND: temporalFields.add(ChronoField.MILLI_OF_SECOND);
            case SECOND: temporalFields.add(ChronoField.SECOND_OF_MINUTE);
            case MINUTE: temporalFields.add(ChronoField.MINUTE_OF_HOUR);
            case HOUR: temporalFields.add(ChronoField.HOUR_OF_DAY);
            case DAY: temporalFields.add(ChronoField.DAY_OF_MONTH);
            case WEEK: temporalFields.add(ChronoField.ALIGNED_WEEK_OF_MONTH);
            case MONTH: temporalFields.add(ChronoField.MONTH_OF_YEAR);
            case YEAR: temporalFields.add(ChronoField.YEAR);
        }
    }

    @Override
    public boolean shouldGroup() {
        return getSelected() != null;
    }
}
