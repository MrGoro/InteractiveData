package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import org.glassfish.jersey.server.model.Resource;

/**
 * @author Philipp Schürmann
 */
public class LineChartApiBuilder extends AbstractApiBuilder<LineChartDefinition> {

    protected final Resource.Builder resourceBuilder = Resource.builder();

    @Override
    public ApiBuilder getBuilder(LineChartDefinition chartDefinition) {
        return new LineChartApiBuilder(chartDefinition);
    }

    private LineChartApiBuilder(LineChartDefinition lineChartDefinition) {
        super(lineChartDefinition);
    }

    @Override
    public AbstractRequestHandler getRequestHandler() {
        return new LineChartRequestHandler(chartDefinition);
    }
}