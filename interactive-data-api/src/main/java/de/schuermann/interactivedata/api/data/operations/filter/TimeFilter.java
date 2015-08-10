package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.service.annotations.FilterService;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

/**
 * @author Philipp Schürmann
 */
@FilterService
public class TimeFilter extends Filter<TimeFilterData> {

    public TimeFilter(String fieldName) {
        super(fieldName);
    }

    public TimeFilter(String fieldName, TimeFilterData filterData) {
        super(fieldName, filterData);
    }

    @Override
    protected <T> boolean test(T t) {
        try {
            String value = BeanUtils.getProperty(t, fieldName);
            Instant date = Instant.parse(value);
            return date.isAfter(getFilterData().getStart()) && date.isBefore(getFilterData().getEnd());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }
}
