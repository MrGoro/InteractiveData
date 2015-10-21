package com.github.mrgoro.interactivedata.spring.sample.repository;

import com.github.mrgoro.interactivedata.spring.sample.data.Temperature;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing temperature from database.
 *
 * @author Philipp Schürmann
 */
public interface TemperatureRepository extends CrudRepository<Temperature, Long>, JpaSpecificationExecutor {
}