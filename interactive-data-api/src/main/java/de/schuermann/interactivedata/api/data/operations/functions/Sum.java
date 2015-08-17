package de.schuermann.interactivedata.api.data.operations.functions;

import de.schuermann.interactivedata.api.data.operations.RequestData;

/**
 * @author Philipp Schürmann
 */
public abstract class Sum extends Function {
    public Sum(String fieldName, Class fieldClass, RequestData requestData) {
        super(fieldName, fieldClass, requestData);
    }
}
