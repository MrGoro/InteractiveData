package de.schuermann.interactivedata.spring.sample.repository;

import de.schuermann.interactivedata.spring.sample.data.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing users from database.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface PersonRepository extends CrudRepository<Person, Long>, JpaSpecificationExecutor {
}