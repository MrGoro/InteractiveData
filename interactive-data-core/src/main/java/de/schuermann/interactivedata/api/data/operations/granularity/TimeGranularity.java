package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.operations.EmptyRequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Philipp Schürmann
 */
public class TimeGranularity extends Granularity<TimeGranularityData, EmptyRequestData> {

    public TimeGranularity(String fieldName, Class fieldClass, TimeGranularityData requestData, EmptyRequestData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    protected Object group(DataObject dataObject) {
        Object value = dataObject.getOptionalProperty(getFieldName()).orElseThrow(
                () -> new ChartDefinitionException("Data with field name [" + getFieldName() + "] must not be null")
        );
        if (Instant.class.isAssignableFrom(value.getClass())) {
            Instant instant = (Instant) value;
            return group(instant);
        } else if (TemporalAccessor.class.isAssignableFrom(value.getClass())) {
            TemporalAccessor temporalAccessor = (TemporalAccessor) value;
            return group(temporalAccessor);
        } else if (Date.class.isAssignableFrom(value.getClass())) {
            Date date = (Date) value;
            return group(date);
        } else if (String.class.isAssignableFrom(value.getClass())) {
            String date = (String) value;
            return group(date);
        }
        throw new ChartDefinitionException("Object [" + dataObject + "] cannot be mapped to a Time type");
    }

    /**
     * Group by a date of type String.
     * <p>
     * Date String must have a format of e.g. {@code 2007-12-03T10:15:30}
     * <p>
     * The string must represent a valid date-time and is parsed using
     * {@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     *
     * @implNote Parses the String value to a LocalDateTime and uses group(TemporalAccessor).
     *
     * @param date Date String
     * @return Group Object
     */
    private Object group(String date) {
        try {
            return group(LocalDateTime.parse(date));
        } catch (DateTimeParseException e) {
            throw new ChartDefinitionException(
                    "Text [" + date + "] cannot be parsed to a date. " +
                    "Provide dates of the format 2007-12-03T10:15:30. " +
                    "See DateTimeFormatter#ISO_LOCAL_DATE_TIME.", e);
        }
    }

    private Object group(Instant instant) {
        return group(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    private Object group(Date date) {
        return group(date.toInstant());
    }

    private Object group(TemporalAccessor temporalAccessor) {
        switch (getRequestData().getSelected()) {
            case MILLISECOND:
                LocalDateTime.of(
                        temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                        temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY),
                        temporalAccessor.get(ChronoField.MINUTE_OF_HOUR), temporalAccessor.get(ChronoField.SECOND_OF_MINUTE),
                        temporalAccessor.get(ChronoField.MILLI_OF_SECOND));
            case SECOND:
                LocalDateTime.of(
                        temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                        temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY),
                        temporalAccessor.get(ChronoField.MINUTE_OF_HOUR), temporalAccessor.get(ChronoField.SECOND_OF_MINUTE));
            case MINUTE:
                return LocalDateTime.of(
                        temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                        temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY),
                        temporalAccessor.get(ChronoField.MINUTE_OF_HOUR));
            case HOUR:
                return LocalDateTime.of(
                        temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                        temporalAccessor.get(ChronoField.DAY_OF_MONTH), temporalAccessor.get(ChronoField.HOUR_OF_DAY), 0);
            case DAY:
                return LocalDate.of(temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR),
                        temporalAccessor.get(ChronoField.DAY_OF_MONTH));
            case WEEK:
                return Arrays.asList(Year.from(temporalAccessor), temporalAccessor.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
            case MONTH:
                return YearMonth.from(temporalAccessor);
            case YEAR:
                return Year.from(temporalAccessor);
            default:
                return LocalDateTime.from(temporalAccessor);
        }
    }


}
