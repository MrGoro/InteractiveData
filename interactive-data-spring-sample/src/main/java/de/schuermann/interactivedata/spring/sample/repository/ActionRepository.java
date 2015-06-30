package de.schuermann.interactivedata.spring.sample.repository;

import de.schuermann.interactivedata.spring.sample.data.Action;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing actions from database.
 *
 * @author Philipp Schürmann
 */
public interface ActionRepository extends CrudRepository<Action, Long> {
}