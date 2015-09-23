package com.github.mrgoro.interactivedata.api.chart.definitions;

import com.github.mrgoro.interactivedata.api.chart.data.ChartData;

/**
 * Interface defining a post processor that is capable of processing chart data to enhance the
 * data more further before sending it over the api.
 *
 * @param <T> Type of the Parameter it can handle
 * @param <R> Return type of the function
 * @author Philipp Sch&uuml;rmann
 */
public interface ChartPostProcessor<T extends ChartData, R> {

    /**
     * Enhance the chart data and return the result.
     *
     * @param data Chart data to enhance
     * @return Enhanced chart data
     */
    R process(T data);

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T extends ChartData> ChartPostProcessor<T, T> identity() {
        return t -> t;
    }

}
