package com.github.mrgoro.interactivedata.spring.data;

import com.github.mrgoro.interactivedata.api.data.source.StreamDataSource;
import com.github.mrgoro.interactivedata.api.data.source.DataSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Basic Implementation of a {@link DataSource} that uses the
 * {@link JpaSpecificationExecutor} of the Spring Framework for querying data from a database.
 *
 * To use this data source you have to override this abstract class and at least implement the method
 * {@link #getRepository()} which should return a repository that is capable of executing {@link Specification}s
 * based on the JPA criteria API.
 *
 * Spring Data supports to provide an {@link JpaSpecificationExecutor} by just specifying a custom interface of the form:
 * <pre>
 *     {@code
 *     public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor {
 *     }
 *     }
 * </pre>
 * This provides an {@link JpaSpecificationExecutor} for the Entity User with a primary key of type Long.
 *
 * The implementation of the data source will look like this:
 * <pre>
 * <code>
 *     public class UserDataSource extends JpaSpecificationDataSource {
 *
 *        {@literal @}Autowired
 *         private UserRepository userRepository;
 *
 *        {@literal @}Override
 *         protected JpaSpecificationExecutor getRepository() {
 *             return userRepository;
 *         }
 *     }
 * </code>
 * </pre>
 *
 * @author Philipp Sch&uuml;rmann
 */
public abstract class JpaSpecificationDataSource<T> extends StreamDataSource {

    protected abstract JpaSpecificationExecutor<T> getRepository();

    @SuppressWarnings("unchecked")
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
