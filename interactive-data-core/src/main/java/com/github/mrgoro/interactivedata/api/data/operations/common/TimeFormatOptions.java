package com.github.mrgoro.interactivedata.api.data.operations.common;

import com.github.mrgoro.interactivedata.api.data.operations.OperationData;
import com.github.mrgoro.interactivedata.api.data.operations.filter.TimeFilter;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.TimeGranularity;

import java.time.format.DateTimeFormatter;

/**
 * Data class for options that set a {@link DateTimeFormatter} with a pattern.
 *
 * @see TimeGranularity
 * @see TimeFilter
 * @author Philipp Sch&uuml;rmann
 */
public class TimeFormatOptions implements OperationData {

    private String pattern = "2011-12-03T10:15:30";

    // No setter - only for internal use / performance => only create once
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TimeFormatOptions() {
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public boolean hasData() {
        return true;
    }

    @Override
    public String toString() {
        return "TimeFilterOptions{" +
                "pattern='" + pattern + '\'' +
                ", formatter=" + formatter +
                '}';
    }
}