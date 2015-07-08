package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import org.glassfish.jersey.server.model.Resource;

/**
 * @author Philipp Schürmann
 */
public class LineChartApiBuilder extends AbstractApiBuilder<LineChartDefinition> {

    protected final Resource.Builder resourceBuilder = Resource.builder();

    public LineChartApiBuilder(LineChartDefinition lineChartDefinition) {
        super(lineChartDefinition);
    }
}