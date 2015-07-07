package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.types.line.Axis;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class LineChartApiBuilder {

    protected final Resource.Builder resourceBuilder = Resource.builder();

    private List<Axis> axis = new ArrayList<>();

    public LineChartApiBuilder(String path) {
        resourceBuilder.path(path);
    }

    public LineChartApiBuilder addAxis(Axis axis) {
        this.axis.add(axis);
        return this;
    }

    private void addMetaInformation() {
        final ResourceMethod.Builder methodBuilder = resourceBuilder.addMethod("GET");
        methodBuilder
            .produces(MediaType.TEXT_PLAIN_TYPE)
            .handledBy(new Inflector<ContainerRequestContext, String>() {
                @Override
                public String apply(ContainerRequestContext containerRequestContext) {
                    return "Hello World!";
                }
            });
        methodBuilder.build();
    }

    public Resource build() {
        addMetaInformation();
        return resourceBuilder.build();
    }
}