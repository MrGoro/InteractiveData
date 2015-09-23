package com.github.mrgoro.interactivedata.spring.sample.repository;

import com.github.mrgoro.interactivedata.spring.sample.data.Action;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA Repository for accessing actions from database.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface ActionRepository extends CrudRepository<Action, Long>, JpaSpecificationExecutor {
}