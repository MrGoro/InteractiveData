package de.schuermann.interactivedata.api.chart.annotations;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;

/**
 * @author Philipp Schürmann
 */
public @interface FilterDef {

    String fieldName();

    Class<?> fieldClass();

    Class<? extends Filter<?>> filter();

    Option[] options() default {};
}
