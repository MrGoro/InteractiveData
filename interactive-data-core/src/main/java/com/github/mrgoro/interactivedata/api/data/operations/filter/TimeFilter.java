package com.github.mrgoro.interactivedata.api.data.operations.filter;

import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.operations.OperationData;
import com.github.mrgoro.interactivedata.api.data.operations.common.TimeFormatOptions;
import com.github.mrgoro.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Filter that checks if a data object is inside a period of time.
 * The period is specified by the start and end in {@link TimeFilterData}.
 * <p>
 * Data is restricted to the following types: {@link Instant}, {@link LocalDateTime}, {@link LocalDate}, {@link Date}
 * and all classes that implement the {@link TemporalAccessor} interface. Strings are supported and parsed. You can
 * specify the pattern for parsing in the filters options. Default is {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
 *
 * <blockquote>
 *     <b>Request Data:</b><br>
 *     start - 2015-01-01T01:00:00 <br>
 *     end - 2015-01-31T23:59:59
 * </blockquote>
 * <blockquote>
 *     <b>Options:</b><br>
 *     pattern - '{@code d MMM uuuu}' for '3 Dec 2011', details in {@link DateTimeFormatterBuilder#appendPattern(String)}
 * </blockquote>
 *
 * @see TimeFilterData
 * @see DateTimeFormatterBuilder#appendPattern(String)
 * @author Philipp Sch&uuml;rmann
 */
public class TimeFilter extends Filter<TimeFilter.TimeFilterData, TimeFormatOptions> {

    public TimeFilter(String fieldName, Class fieldClass, TimeFilter.TimeFilterData requestData, TimeFormatOptions options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        Object value = dataObject.getOptionalProperty(getFieldName()).orElseThrow(
            () -> new ChartDefinitionException("Data with field name [" + getFieldName() + "] must not be null")
        );
        if(Instant.class.isAssignableFrom(value.getClass())) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            return test(localDateTime);
        } else if(Instant.class.isAssignableFrom(value.getClass())) {
            Instant instant = (Instant) value;
            return test(instant);
        } else if(TemporalAccessor.class.isAssignableFrom(value.getClass())) {
            TemporalAccessor temporalAccessor = (TemporalAccessor) value;
            return test(LocalDateTime.from(temporalAccessor));
        } else if(Date.class.isAssignableFrom(value.getClass())) {
            Date date = (Date) value;
            return test(date);
        } else if(String.class.isAssignableFrom(getFieldClass())) {
            return test(value.toString());
        }
        return false;
    }

    /**
     * Filter by date of type String with parsing.
     * <p>
     * Date String must have the format specified in options data (see {@link TimeFormatOptions}). Default format
     * is {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     *
     * @param text Date as String
     * @return true when object should be included, false if not
     */
    private boolean test(String text) {
        try {
            return test(LocalDateTime.parse(text, getOptions().getFormatter()));
        } catch (DateTimeParseException e) {
            throw new ChartDefinitionException(
                    "Text [" + text + "] cannot be parsed to a date. " +
                            "Provide dates of the format " + getOptions().getPattern(), e);
        }
    }

    private boolean test(Instant instant) {
        return test(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    private boolean test(TemporalAccessor temporalAccessor) {
        return test(LocalDateTime.from(temporalAccessor));
    }

    private boolean test(LocalDateTime localDateTime) {
        return localDateTime.isAfter(getRequestData().getStart()) && localDateTime.isBefore(getRequestData().getEnd());
    }

    private boolean test(Date date) {
        return test(date.toInstant());
    }

    /**
     * Specification of request parameters for {@link TimeFilter}.
     *
     * @see TimeFilter
     */
    public static class TimeFilterData implements OperationData {

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
}