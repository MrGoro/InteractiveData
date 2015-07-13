package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * @author Philipp Schürmann
 */
public class LineChartApiBuilder extends AbstractApiBuilder<LineChartDefinition> {

    public LineChartApiBuilder(ApplicationContext applicationContext, LineChartDefinition lineChartDefinition) {
        super(applicationContext, lineChartDefinition);
    }

    @Override
    public Inflector<ContainerRequestContext, ChartData> getRequestHandler() {
        return new LineChartRequestHandler(applicationContext, chartDefinition);
    }
}