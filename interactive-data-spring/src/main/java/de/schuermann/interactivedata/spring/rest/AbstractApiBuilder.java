package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.LineChartDefinition;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractApiBuilder<T extends AbstractChartDefinition> implements ApiBuilder<T> {

    protected final Resource.Builder resourceBuilder = Resource.builder();
    protected ApplicationContext applicationContext;
    protected T chartDefinition;

    protected AbstractApiBuilder(ApplicationContext applicationContext, T chartDefinition) {
        this.applicationContext = applicationContext;
        this.chartDefinition = chartDefinition;
    }

    protected void addMetaInformation() {
        final ResourceMethod.Builder methodBuilder = resourceBuilder.addMethod("OPTIONS");
        methodBuilder
                .produces(MediaType.APPLICATION_JSON)
                .handledBy(containerRequestContext -> chartDefinition);
        methodBuilder.build();
    }

    protected void addDataMethod() {
        final ResourceMethod.Builder methodBuilder = resourceBuilder.addMethod("GET");
        methodBuilder
                .produces(MediaType.APPLICATION_JSON)
                .handledBy(getRequestHandler());
        methodBuilder.build();
    }

    public abstract Inflector<ContainerRequestContext, ChartData> getRequestHandler();

    public Resource build() {
        resourceBuilder.path(chartDefinition.getName());
        addMetaInformation();
        addDataMethod();
        return resourceBuilder.build();
    }

}
