package com.github.mrgoro.interactivedata.spring.data;

import com.github.mrgoro.interactivedata.api.data.operations.filter.Filter;
import com.github.mrgoro.interactivedata.api.data.source.DataSource;
import com.github.mrgoro.interactivedata.api.data.source.StreamDataSource;
import com.github.mrgoro.interactivedata.api.util.ReflectionUtil;
import com.github.mrgoro.interactivedata.spring.data.processors.FilterToPredicateProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Basic Implementation of a {@link DataSource} that uses the
 * {@link JpaSpecificationExecutor} of the Spring Framework for querying data from a database.
 * <p>
 * To use this data source you have to override this abstract class and at least implement the method
 * {@link #getRepository()} which should return a repository that is capable of executing {@link Specification}s
 * based on the JPA criteria API.
 * <p>
 * Spring Data supports to provide an {@link JpaSpecificationExecutor} by just specifying a custom interface of the form:
 * <pre>
 *     {@code
 *     public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor {
 *     }
 *     }
 * </pre>
 * This provides an {@link JpaSpecificationExecutor} for the Entity User with a primary key of type Long.
 * <p>
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

    private static final Log log = LogFactory.getLog(JpaSpecificationDataSource.class);

    @Autowired
    private List<FilterToPredicateProcessor<? extends Filter<?, ?>>> filterToPredicateProcessors;

    /**
     * Return the repository that is capable of executing {@link Specification JpaSpecification}.
     * <p>
     * The repository must implement the {@link JpaSpecificationExecutor} interface. The easiest solution
     * to implement this interface is using Spring Data JPA Repositories by implementing the interfaces
     * {@link org.springframework.data.repository.CrudRepository} or
     * {@link org.springframework.data.repository.PagingAndSortingRepository}.
     *
     * @return Repository implementing JpaSpecificationExecutor
     */
    protected abstract JpaSpecificationExecutor<T> getRepository();

    /**
     * Query the data from the repository using the specification.
     *
     * @return List of data objects
     * @see #getRepository()
     * @see #getSpecification()
     */
    protected List<T> getData() {
        return getRepository().findAll(getSpecification());
    }

    /**
     * Provide the queried data as a {@link Stream}.
     *
     * @return Stream of data objects
     * @see #getData()
     */
    @Override
    protected Stream getDataStream() {
        return getData().parallelStream();
    }

    /**
     * Get the specification for the current {@link com.github.mrgoro.interactivedata.api.data.DataRequest}.
     * <p>
     * The specification is used to query the database.
     *
     * @return Specification
     */
    protected Specification<T> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Iterator<Filter<?, ?>> iterator = dataRequest.getFilter().iterator();
            while (iterator.hasNext()) {
                Filter<?, ?> filter = iterator.next();
                this.getPredicate(filter, root, query, cb).ifPresent(
                        predicate -> {
                            log.debug("Predicate replaced the Filter [" + filter.toString() + "]");
                            predicates.add(predicate);
                            iterator.remove();
                        });
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Get a Predicate from a given {@link Filter}.
     *
     * @param filter Filter the predicate should replace
     * @param root   Root
     * @param query  CriteriaQuery
     * @param cb     CriteriaBuilder
     * @param <F>    Type of the Filter
     * @return Optional with Predicate if the filter can be replaced or empty if the filter still has to operate
     */
    private <F extends Filter<?, ?>> Optional<Predicate> getPredicate(F filter, Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Optional<FilterToPredicateProcessor<F>> filterToPredicateProcessor = getFilterToPredicateProcessor(filter);
        return filterToPredicateProcessor.flatMap(
                processor -> processor.toPredicate(filter, root, query, cb)
        );
    }

    /**
     * Get a {@link FilterToPredicateProcessor} for the given {@link Filter}.
     * <p>
     * Uses an {@link Optional} that is empty when there is no FilterToPredicateProcessor for the given Filter.
     *
     * @param filter Filter the processor should by capable of processing
     * @param <F>    Type of the Filter
     * @return FilterToPredicateProcessor capable of processing the given Filter
     */
    @SuppressWarnings("unchecked")
    private <F extends Filter<?, ?>> Optional<FilterToPredicateProcessor<F>> getFilterToPredicateProcessor(F filter) {
        Optional<FilterToPredicateProcessor<? extends Filter<?, ?>>> filterProcessor = filterToPredicateProcessors.stream().filter(
                processor -> ReflectionUtil.isGenericImplementation(processor.getClass(), FilterToPredicateProcessor.class, filter.getClass())
        ).findFirst();

        return filterProcessor.map(
                processor -> (FilterToPredicateProcessor<F>) processor
        );
    }

}
