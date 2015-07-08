package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import org.glassfish.jersey.server.model.Resource;

/**
 * @author Philipp Schürmann
 */
public interface ApiBuilder<D extends AbstractChartDefinition> {

    ApiBuilder getBuilder(D chartDefinition);

    Resource build();

}