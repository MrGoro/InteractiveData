package de.schuermann.interactivedata.api.chart.processors;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;

import java.lang.annotation.Annotation;

/**
 * Interface Definition for a Processor that is able to process the information of an Annotation
 * into a ChartDefinition.
 *
 * @author Philipp Schürmann
 */
public interface AnnotationProcessor<T extends Annotation> {

    /**
     * Generates the Definition of the Chart from the Annotation an a name.
     *
     * @param name Name of the Chart
     * @param annotation Annotation of the Chart
     * @return Chart Definition
     */
    AbstractChartDefinition process(String name, T annotation);

}