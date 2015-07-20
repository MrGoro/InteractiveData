package de.schuermann.interactivedata.spring.sample.datasource;

import de.schuermann.interactivedata.spring.data.JpaSpecificationDataSource;
import de.schuermann.interactivedata.spring.sample.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Philipp Schürmann
 */
@Repository
public class ActionDataSource extends JpaSpecificationDataSource {

    @Autowired
    private ActionRepository actionRepository;

    @Override
    protected JpaSpecificationExecutor getRepository() {
        return actionRepository;
    }
}
