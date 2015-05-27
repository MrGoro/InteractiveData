package de.schuermann.interactivedata.sample.repository;

import de.schuermann.interactivedata.sample.data.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing users from database.
 *
 * Created by philipp on 27.05.2015.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}