package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.annotations.FilterService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author Philipp Schürmann
 */
@FilterService
public class TimeFilter extends Filter<TimeFilterData> {

    public TimeFilter(String fieldName, Class fieldClass, TimeFilterData filterData) {
        super(fieldName, fieldClass, filterData);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        Object value = dataObject.getProperty(getFieldName());
        if(Instant.class.isAssignableFrom(value.getClass())) {
            Instant instant = (Instant) value;
            return test(instant);
        } else if(TemporalAccessor.class.isAssignableFrom(value.getClass())) {
            TemporalAccessor temporalAccessor = (TemporalAccessor) value;
            return test(temporalAccessor);
        } else if(Date.class.isAssignableFrom(value.getClass())) {
            Date date = (Date) value;
            return test(date);
        }
        return false;
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
}