package de.schuermann.interactivedata.spring.sample.datasource;

import de.schuermann.interactivedata.spring.data.JpaSpecificationDataSource;
import de.schuermann.interactivedata.spring.sample.repository.LocationRepository;
import de.schuermann.interactivedata.spring.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Repository
public class LocationDataSource extends JpaSpecificationDataSource {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    protected JpaSpecificationExecutor getRepository() {
        return locationRepository;
    }
}
