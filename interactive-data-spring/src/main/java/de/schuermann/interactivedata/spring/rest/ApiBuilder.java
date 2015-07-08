package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import org.glassfish.jersey.server.model.Resource;

/**
 * Interface that has to bei implemented by ApiBuilders to be found for building a {@Link Resource} from a
 * {@Link AbstractChartDefinition} during application startup.
 *
 * @author Philipp Schürmann
 */
public interface ApiBuilder<D extends AbstractChartDefinition> {

    /**
     * Builds the Api and return the corresponding {@Link Resource}
     * @return Resource for the API
     */
    Resource build();

}