package de.schuermann.interactivedata.spring.sample.controller;

import de.schuermann.interactivedata.spring.sample.UserService;
import de.schuermann.interactivedata.spring.sample.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for a RESTful API to output all Users.
 *
 * @author Philipp Schürmann
 */
@RestController
@RequestMapping("/rest")
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userService.findAll();
    }
}
