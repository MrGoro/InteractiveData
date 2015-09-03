package de.schuermann.interactivedata.api.data.operations.granularity;

import de.schuermann.interactivedata.api.data.operations.OperationData;
import de.schuermann.interactivedata.api.data.operations.common.TimeFormatOptions;
import de.schuermann.interactivedata.api.data.bean.DataObject;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Date;

/**
 * Granularity based on levels of time.
 * <p>
 * The level of granularity is specified by the request attribute {@link TimeGranularityData#selected} and has one
 * of the following values: MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR (see {@link TimeGranularityData.STEP})
 * <p>
 * When grouping at a specific level only date information at that level is used for equality checking. For example if
 * the level is month, information on the day, hour, minute etc are stripped.
 * <p>
 * Data is restricted to the following types: {@link Instant}, {@link LocalDateTime}, {@link LocalDate}, {@link Date}
 * and all classes that implement the {@link TemporalAccessor} interface. Strings are supported and parsed. You can
 * specify the pattern for parsing in the filters options. Default is {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
 *
 * <blockquote>
 *     <b>Request Data:</b><br>
 *     selected - {@link TimeGranularityData.STEP} e.g. MONTH
 * </blockquote>
 * <blockquote>
 *     <b>Options:</b><br>
 *     pattern - '{@code d MMM uuuu}' for '3 Dec 2011', details in {@link DateTimeFormatterBuilder#appendPattern(String)}
 * </blockquote>
 *
 * @author Philipp Sch&uuml;rmann
 */
public class TimeGranularity extends Granularity<TimeGranularity.TimeGranularityData, TimeFormatOptions> {

    public TimeGranularity(String fieldName, Class fieldClass, TimeGranularityData requestData, TimeFormatOptions options) {
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
     * Group by a date of type String with parsing.
     * <p>
     * Date String must have the format specified in options data (see {@link TimeFormatOptions}). Default format
     * is {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     *
     * @param text Date String
     * @return Group Object
     */
    private Object group(String text) {
        try {
            return group(LocalDateTime.parse(text));
        } catch (DateTimeParseException e) {
            throw new ChartDefinitionException(
                    "Text [" + text + "] cannot be parsed to a date. " +
                            "Provide dates of the format " + getOptions().getPattern(), e);
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

    /**
     * Specification of request parameters for {@link TimeGranularity}.
     *
     * @see TimeGranularity
     */
    public static class TimeGranularityData implements OperationData {

        @Override
        public boolean hasData() {
            return getSelected() != null;
        }

        /**
         * Levels of granularity
         */
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
}
