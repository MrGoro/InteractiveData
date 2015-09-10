package de.schuermann.interactivedata.spring.sample.datasource;

import de.schuermann.interactivedata.spring.data.JpaSpecificationDataSource;
import de.schuermann.interactivedata.spring.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Sample Implementation to demonstrate the use of {@link JpaSpecificationDataSource} for the user table in the database.
 * <p>
 * This makes use of a existing Spring Data Repository that also implements the {@link JpaSpecificationExecutor} interface.
 * <p>
 * To make the data source work the {@link #getRepository()} method only has to return the injected repository.
 * <p>
 * The data used for this example is defined inside the file data.sql. It is populated to an in-memory database during
 * startup. This data source is independent from the database as it uses JPA. In production it can use databases
 * like MySQL, Oracle, PostgreSQL etc.
 *
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
