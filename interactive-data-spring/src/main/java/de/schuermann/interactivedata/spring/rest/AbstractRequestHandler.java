package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.data.DataSource;
import org.glassfish.jersey.process.Inflector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractRequestHandler<T extends AbstractChartDefinition, D extends ChartData> implements Inflector<ContainerRequestContext, ChartData> {

    protected ApplicationContext applicationContext;
    protected T chartDefinition;

    public AbstractRequestHandler(ApplicationContext applicationContext, T chartDefinition) {
        this.applicationContext = applicationContext;
        this.chartDefinition = chartDefinition;
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    @Override
    public D apply(ContainerRequestContext containerRequestContext) {
        extract(containerRequestContext);
        return getData();
    }

    protected void extract(ContainerRequestContext containerRequestContext) {
        // TODO extract request data from context
    }

    protected DataSource getDataSource() {
        return (DataSource) applicationContext.getBean(chartDefinition.getDataSource());
    }

    protected abstract D getData();

}
