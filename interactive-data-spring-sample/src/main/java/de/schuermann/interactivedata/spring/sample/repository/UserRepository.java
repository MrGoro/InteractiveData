package de.schuermann.interactivedata.spring.sample.repository;

import de.schuermann.interactivedata.spring.sample.data.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing users from database.
 *
 * Created by philipp on 27.05.2015.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}