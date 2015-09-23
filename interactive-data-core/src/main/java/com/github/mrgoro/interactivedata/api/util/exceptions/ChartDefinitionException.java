package com.github.mrgoro.interactivedata.api.util.exceptions;

/**
 * Exception indicating that the definition of the chart is corrupt.
 *
 * See the message for detail information on how to solve this issue.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class ChartDefinitionException extends RuntimeException {

    public ChartDefinitionException(String message) {
        super(message);
    }

    public ChartDefinitionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
