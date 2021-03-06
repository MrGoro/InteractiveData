package com.github.mrgoro.interactivedata.api.chart.processors;

import com.github.mrgoro.interactivedata.api.chart.data.ChartData;
import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.service.annotations.AnnotationProcessorService;

import java.lang.annotation.Annotation;

/**
 * Interface definition for a processor that is able to process the information of an annotation
 * into a {@link AbstractChartDefinition ChartDefinition}.
 *
 * @author Philipp Sch&uuml;rmann
 */
@AnnotationProcessorService
public interface AnnotationProcessor<T extends Annotation> {

    /**
     * Generates the definition of the chart from the Annotation an a name.
     *
     * @param annotation Annotation of the chart
     * @return Chart definition
     */
    AbstractChartDefinition<?, ? extends ChartData> process(T annotation);

}