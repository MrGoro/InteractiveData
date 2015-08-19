package de.schuermann.interactivedata.api.chart.annotations;

import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;

/**
 * @author Philipp Schürmann
 */
public @interface OperationDef {

    String fieldName();
    Class<?> fieldClass();
    Class<? extends Granularity<?>> granularity();
    Option[] options() default {};

    FunctionDef[] functions();
}
