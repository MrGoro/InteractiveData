package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Philipp Schürmann
 */
public class TimeGranularity extends Granularity<TimeGranularityData> {

    public TimeGranularity(String fieldName, Class fieldClass, TimeGranularityData granularityData) {
        super(fieldName, fieldClass, granularityData);
    }

    @Override
    protected Object group(DataObject dataObject) {
        Object value = dataObject.getProperty(getFieldName());
        if(Instant.class.isAssignableFrom(value.getClass())) {
            Instant instant = (Instant) value;
            return group(instant);
        } else if(TemporalAccessor.class.isAssignableFrom(value.getClass())) {
            TemporalAccessor temporalAccessor = (TemporalAccessor) value;
            return group(temporalAccessor);
        } else if(Date.class.isAssignableFrom(value.getClass())) {
            Date date = (Date) value;
            return group(date);
        }
        return null;
    }

    private Object group(Instant instant) {
        return group(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    private Object group(TemporalAccessor temporalAccessor) {
        switch (getRequestData().getSelected()) {
            case MILLISECOND: LocalDateTime.of(
                    temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                    temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY),
                    temporalAccessor.get(ChronoField.MINUTE_OF_HOUR), temporalAccessor.get(ChronoField.SECOND_OF_MINUTE),
                    temporalAccessor.get(ChronoField.MILLI_OF_SECOND));
            case SECOND: LocalDateTime.of(
                    temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                    temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY),
                    temporalAccessor.get(ChronoField.MINUTE_OF_HOUR), temporalAccessor.get(ChronoField.SECOND_OF_MINUTE));
            case MINUTE: return LocalDateTime.of(
                    temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                    temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY),
                    temporalAccessor.get(ChronoField.MINUTE_OF_HOUR));
            case HOUR: return LocalDateTime.of(
                    temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                    temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY), 0);
            case DAY: return LocalDate.of(temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                    temporalAccessor.get(ChronoField.DAY_OF_MONTH));
            case WEEK: return Arrays.asList(Year.from(temporalAccessor), temporalAccessor.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
            case MONTH: return YearMonth.from(temporalAccessor);
            case YEAR: return Year.from(temporalAccessor);
            default: return LocalDateTime.from(temporalAccessor);
        }
    }

    private Object group(Date date) {
        return group(date.toInstant());
    }


}
