package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import org.glassfish.jersey.process.Inflector;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractRequestHandler<T extends AbstractChartDefinition, D extends ChartData> implements Inflector<ContainerRequestContext, ChartData> {

    private T chartDefinition;

    public AbstractRequestHandler(T chartDefinition) {
        this.chartDefinition = chartDefinition;
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    @Override
    public D apply(ContainerRequestContext containerRequestContext) {
        return getData();
    }

    protected abstract D getData();

}
