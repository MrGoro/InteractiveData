package de.schuermann.interactivedata.spring.data;

import de.schuermann.interactivedata.api.data.DataSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Basic Implementation of a DataSource using Spring Data Repositories.
 *
 * @author Philipp Schürmann
 */
public abstract class JpaSpecificationDataSource<T> implements DataSource {

    @Override
    public List getData(Map filterMap, Map granularityMap, Map functionMap) {
        JpaSpecificationExecutor<T> repository = getRepository();
        return repository.findAll(where(getSpecification()));
    }

    protected abstract JpaSpecificationExecutor<T> getRepository();

    public Specification<T> getSpecification() {
        return null;
    }

}
