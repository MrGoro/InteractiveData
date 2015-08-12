package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.annotations.FilterService;

import java.time.Instant;
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
    protected boolean test(DataObject t) {
        Object value = t.getProperty(getFieldName());
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
        return instant.isAfter(getFilterData().getStart()) && instant.isBefore(getFilterData().getEnd());
    }

    private boolean test(TemporalAccessor temporalAccessor) {
        Instant instant = Instant.from(temporalAccessor);
        return test(instant);
    }

    private boolean test(Date date) {
        return test(date.toInstant());
    }
}
