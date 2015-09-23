package com.github.mrgoro.interactivedata.spring.sample.controller;

import com.github.mrgoro.interactivedata.spring.sample.repository.ActionRepository;
import com.github.mrgoro.interactivedata.spring.sample.data.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Sample for demonstrating "details on demand" to access all or a specific action resource.
 *
 * @author Philipp Sch&uuml;rmann
 */
@RestController
@RequestMapping("/rest")
public class ActionRestController {

    @Autowired
    private ActionRepository actionRepository;

    @RequestMapping(
            value = "/actions",
            method = RequestMethod.GET
    )
    @Cacheable("/rest/actions")
    public List<Action> getAllActions() {
        List<Action> actions = new ArrayList<>();
        actionRepository.findAll().forEach(actions::add);
        return actions;
    }

    @RequestMapping(
            value = "/action/{id}",
            method = RequestMethod.GET
    )
    @Cacheable("/rest/action/id")
    public ResponseEntity<Action> getActionById(@PathVariable long id) {
        return Optional.ofNullable(actionRepository.findOne(id))
            .map(
                action -> new ResponseEntity<>(action, HttpStatus.OK)
            ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
    }
}
