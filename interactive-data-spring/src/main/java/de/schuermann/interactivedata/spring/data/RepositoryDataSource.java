package de.schuermann.interactivedata.spring.data;

import de.schuermann.interactivedata.api.data.DataSource;

import java.util.List;
import java.util.Map;

/**
 * Default Implementation of a DataSource using Spring Data Repositories.
 *
 * @author Philipp Schürmann
 */
public class RepositoryDataSource implements DataSource {
    @Override
    public List getData(Map filterMap, Map granularityMap, Map functionMap) {
        return null;
    }
}
