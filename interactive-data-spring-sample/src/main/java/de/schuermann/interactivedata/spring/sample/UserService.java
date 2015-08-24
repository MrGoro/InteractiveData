package de.schuermann.interactivedata.spring.sample;

import de.schuermann.interactivedata.spring.sample.data.User;
import de.schuermann.interactivedata.spring.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();
        users.forEach(list::add);
        return list;
    }

}
