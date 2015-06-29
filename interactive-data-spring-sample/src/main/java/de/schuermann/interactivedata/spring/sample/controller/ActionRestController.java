package de.schuermann.interactivedata.spring.sample.controller;

import de.schuermann.interactivedata.spring.sample.data.Action;
import de.schuermann.interactivedata.spring.sample.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for a RESTful API to output all Actions.
 *
 * Created by philipp on 27.05.2015.
 */
@RestController
@RequestMapping("/rest")
public class ActionRestController {

    @Autowired
    private ActionRepository actionRepository;

    @RequestMapping(value = "/actions", method = RequestMethod.GET)
    private List<Action> getAllActions() {
        List<Action> list = new ArrayList<>();
        Iterable<Action> actions = actionRepository.findAll();
        actions.forEach(list::add);
        return list;
    }
}
