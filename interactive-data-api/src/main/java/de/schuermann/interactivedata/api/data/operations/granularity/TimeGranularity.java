package de.schuermann.interactivedata.api.data.operations.granularity;

/**
 * @author Philipp Schürmann
 */
public class TimeGranularity implements Granularity<TimeGranularityData> {

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

}
