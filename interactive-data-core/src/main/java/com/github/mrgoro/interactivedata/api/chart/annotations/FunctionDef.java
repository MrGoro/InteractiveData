package com.github.mrgoro.interactivedata.api.chart.annotations;

import com.github.mrgoro.interactivedata.api.data.operations.Operation;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Function;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.Granularity;

/**
 * Definition of a {@link Function}. Functions are used for aggregating data while grouping. Functions are always
 * defined in combination to a {@link Granularity} inside an {@link Operation}.
 *
 * @author Philipp Sch&uuml;rmann
 * @see OperationDef
 * @see Operation
 * @see Granularity
 * @see Function
 */
public @interface FunctionDef {

    /**
     * Name of the field the function operates on.
     *
     * @return Name of the field
     */
    String fieldName();

    /**
     * Name of the field after the data is processed by the function.
     * <p>
     * Useful if multiple different functions operate on the same field.
     *
     * @return Name of the target field
     */
    String targetFieldName() default "";

    /**
     * Type of the field the function operates on.
     * <p>
     * Note that {@link Function Functions} do not support every type. See the documentation of the function for
     * more information about data types supported.
     *
     * @return Type of the field
     */
    Class<?> fieldClass();

    /**
     * Class of the Function. Function class must extend {@link Function}.
     *
     * @return Class of the Filter.
     */
    Class<? extends Function<?, ?>> function();

    /**
     * Options of the operation.
     * <p>
     * Options are key value paris defined within {@link Option} annotation. These options are used to populate
     * the options data object of the function. See the documentation of the function for more information of the
     * available options each function has.
     * <p>
     * Options are optional and do not have to be set.
     *
     * @return Array of options
     */
    Option[] options() default {};
}
