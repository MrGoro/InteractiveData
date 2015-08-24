package de.schuermann.interactivedata.api.chart.definitions;

import de.schuermann.interactivedata.api.chart.data.ChartData;

/**
 * Interface defining a post processor that is capable of processing chart data to enhance the
 * data more further before sending it over the api.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface ChartPostProcessor<T extends ChartData> {

    /**
     * Enhance the chart data and return the result.
     *
     * @param data Chart data to enhance
     * @return Enhanced chart data
     */
    T process(T data);

}
