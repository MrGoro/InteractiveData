package de.schuermann.interactivedata.spring.sample.repository;

import de.schuermann.interactivedata.spring.sample.data.Location;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Philipp Sch&uuml;rmann
 */
public interface LocationRepository extends CrudRepository<Location, Long>, JpaSpecificationExecutor {
}
