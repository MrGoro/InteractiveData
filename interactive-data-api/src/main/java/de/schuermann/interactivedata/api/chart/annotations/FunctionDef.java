package de.schuermann.interactivedata.api.chart.annotations;

import de.schuermann.interactivedata.api.data.operations.functions.Function;

/**
 * @author Philipp Schürmann
 */
public @interface FunctionDef {

    String fieldName();
    String targetFieldName() default "";
    Class<?> fieldClass();
    Class<? extends Function<?>> function();
    Option[] options() default {};
}
