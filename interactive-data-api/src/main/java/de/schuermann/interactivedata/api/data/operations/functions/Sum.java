package de.schuermann.interactivedata.api.data.operations.functions;

/**
 * @author Philipp Schürmann
 */
public abstract class Sum extends Function {

    public Sum(String fieldName, Class fieldClass, FunctionData functionData) {
        super(fieldName, fieldClass, functionData);
    }
}
