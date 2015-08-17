package de.schuermann.interactivedata.api.data.operations.functions;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Philipp Schürmann
 */
public abstract class Average extends Function {

    public Average(String fieldName, Class fieldClass, FunctionData functionData) {
        super(fieldName, fieldClass, functionData);
    }
}
