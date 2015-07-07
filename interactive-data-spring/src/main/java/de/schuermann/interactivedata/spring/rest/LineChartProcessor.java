package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.types.line.LineChart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Philipp Schürmann
 */
public class LineChartProcessor implements AnnotationProcessor<LineChart> {

    private Log log = LogFactory.getLog(LineChartProcessor.class);

    @Override
    public org.glassfish.jersey.server.model.Resource process(String name, LineChart annotation) {
        log.debug("Processing LineChart Annotation: " + annotation.toString());

        LineChartApiBuilder apiBuilder = new LineChartApiBuilder(name);
        return apiBuilder.build();
    }

}
