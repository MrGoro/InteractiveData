package de.schuermann.interactivedata.sample.controller;

import de.schuermann.interactivedata.sample.data.User;
import de.schuermann.interactivedata.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for a RESTful API to output all Users.
 *
 * Created by philipp on 27.05.2015.
 */
@RestController
@RequestMapping("/rest")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();
        users.forEach(list::add);
        return list;
    }
}
