package de.schuermann.interactivedata.api.util.exceptions;

/**
 * Exception indicating that the data provided with the request are corrupt or in a wrong format.
 *
 * See the message for detail information on how to solve this issue.
 *
 * @author Philipp Schürmann
 */
public class RequestDataException extends RuntimeException {

    public RequestDataException(String message) {
        super(message);
    }

    public RequestDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
