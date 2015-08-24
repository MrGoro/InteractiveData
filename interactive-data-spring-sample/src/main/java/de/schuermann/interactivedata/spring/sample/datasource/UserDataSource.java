package de.schuermann.interactivedata.spring.sample.datasource;

import de.schuermann.interactivedata.spring.data.JpaSpecificationDataSource;
import de.schuermann.interactivedata.spring.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Repository
public class UserDataSource extends JpaSpecificationDataSource {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected JpaSpecificationExecutor getRepository() {
        return userRepository;
    }
}
