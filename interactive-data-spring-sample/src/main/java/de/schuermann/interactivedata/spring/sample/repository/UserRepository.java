package de.schuermann.interactivedata.spring.sample.repository;

import de.schuermann.interactivedata.spring.sample.data.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing users from database.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor {
}