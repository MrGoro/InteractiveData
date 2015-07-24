package de.schuermann.interactivedata.api.chart.definitions;

import de.schuermann.interactivedata.api.chart.data.ChartData;

/**
 * @author Philipp Schürmann
 */
@FunctionalInterface
public interface ChartPostProcessor<T extends ChartData> {

    T process(T data);

}
