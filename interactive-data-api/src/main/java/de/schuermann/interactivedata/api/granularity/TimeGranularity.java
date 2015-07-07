package de.schuermann.interactivedata.api.granularity;

/**
 * @author Philipp Sch�rmann
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
