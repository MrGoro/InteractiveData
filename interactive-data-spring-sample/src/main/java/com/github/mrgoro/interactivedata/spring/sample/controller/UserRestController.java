package com.github.mrgoro.interactivedata.spring.sample.controller;

import com.github.mrgoro.interactivedata.spring.sample.repository.PersonRepository;
import com.github.mrgoro.interactivedata.spring.sample.data.Person;
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
 * Sample for demonstrating "details on demand" to access all or a specific user resource.
 *
 * @author Philipp Sch&uuml;rmann
 */
@RestController
@RequestMapping("/rest")
public class UserRestController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(
            value = "/users",
            method = RequestMethod.GET
    )
    @Cacheable("/rest/users")
    public List<Person> getAllUsers() {
        List<Person> actions = new ArrayList<>();
        personRepository.findAll().forEach(actions::add);
        return actions;
    }

    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET
    )
    @Cacheable("/rest/user/id")
    public ResponseEntity<Person> getUserById(@PathVariable long id) {
        return Optional.ofNullable(personRepository.findOne(id))
            .map(
                    user -> new ResponseEntity<>(user, HttpStatus.OK)
            ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
    }
}
