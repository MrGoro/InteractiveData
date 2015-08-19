package de.schuermann.interactivedata.spring.data;

import de.schuermann.interactivedata.api.data.StreamDataSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Basic Implementation of a DataSource using Spring Data Repositories and Processors.
 *
 * @author Philipp Schürmann
 */
public abstract class JpaSpecificationDataSource<T> extends StreamDataSource {

    protected abstract JpaSpecificationExecutor<T> getRepository();

    protected List<Object> getData() {
        return (List<Object>) getRepository().findAll(getSpecification());
    }

    @Override
    protected Stream getDataStream() {
        return getData().parallelStream();
    }

    private Specification<T> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
