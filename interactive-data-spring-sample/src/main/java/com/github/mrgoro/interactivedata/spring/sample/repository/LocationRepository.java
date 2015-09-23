package com.github.mrgoro.interactivedata.spring.sample.repository;

import com.github.mrgoro.interactivedata.spring.sample.data.Location;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing locations from database.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface LocationRepository extends CrudRepository<Location, Long>, JpaSpecificationExecutor {
}
