package com.github.mrgoro.interactivedata.spring.data.processors;

import com.github.mrgoro.interactivedata.api.data.operations.filter.TimeFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Component
public class TimeFilterProcessor implements FilterToPredicateProcessor<TimeFilter> {

    private static final Log log = LogFactory.getLog(TimeFilterProcessor.class);

    @Override
    public Optional<Predicate> toPredicate(TimeFilter filter, Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        log.debug("Builder Predicate for TimeFilter[" + filter.toString() + "]");

        LocalDateTime start = filter.getRequestData().getStart();
        LocalDateTime end = filter.getRequestData().getEnd();
        String fieldName = filter.getFieldName();

        if(start != null && end != null) {
            if (filter.getFieldClass() == LocalDateTime.class) {
                return Optional.of(cb.between(
                        root.get(fieldName),
                        start,
                        end
                ));
            } else if (filter.getFieldClass() == Date.class) {
                return Optional.of(cb.between(
                        root.get(fieldName),
                        Date.from(start.atZone(ZoneId.systemDefault()).toInstant()),
                        Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
                ));
            } else if (filter.getFieldClass() == Instant.class) {
                return Optional.of(cb.between(
                        root.get(fieldName),
                        start.atZone(ZoneId.systemDefault()).toInstant(),
                        end.atZone(ZoneId.systemDefault()).toInstant()
                ));
            }
        }
        return Optional.empty();
    }
}