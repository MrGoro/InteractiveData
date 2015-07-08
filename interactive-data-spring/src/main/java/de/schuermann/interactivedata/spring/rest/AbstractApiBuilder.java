package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractApiBuilder<T extends AbstractChartDefinition> implements ApiBuilder {

    protected final Resource.Builder resourceBuilder = Resource.builder();
    protected final T chartDefinition;

    protected AbstractApiBuilder(T chartDefinition) {
        this.chartDefinition = chartDefinition;
        resourceBuilder.path(chartDefinition.getName());
        addMetaInformation();
    }

    protected void addMetaInformation() {
        final ResourceMethod.Builder methodBuilder = resourceBuilder.addMethod("OPTIONS");
        methodBuilder
                .produces(MediaType.APPLICATION_JSON)
                .handledBy(new Inflector<ContainerRequestContext, AbstractChartDefinition>() {
                    @Override
                    public AbstractChartDefinition apply(ContainerRequestContext containerRequestContext) {
                        return chartDefinition;
                    }
                });
        methodBuilder.build();
    }

    public Resource build() {
        return resourceBuilder.build();
    }

}
