package com.github.mrgoro.interactivedata.api.chart.annotations;

import com.github.mrgoro.interactivedata.api.data.operations.Operation;
import com.github.mrgoro.interactivedata.api.data.operations.functions.Function;
import com.github.mrgoro.interactivedata.api.data.operations.granularity.Granularity;

/**
 * Definition of a {@link Operation} combining a granularity and multiple functions that are used to reduce that
 * data by grouping.
 *
 * @author Philipp Sch&uuml;rmann
 */
public @interface OperationDef {

    /**
     * Name of the field the operation operates on.
     *
     * @return Name of the field
     */
    String fieldName();

    /**
     * Type of the field the operation operates on.
     * <p>
     * Note that {@link Operation Operations} do not support every type. See the documentation of the operation for
     * more information about data types supported.
     *
     * @return Type of the field
     */
    Class<?> fieldClass();

    /**
     * Class of the granularity the operations uses. Granularity classes must extend {@link Granularity}.
     * <p>
     * The granularity specifies the type of grouping.
     *
     * @return Class of the granularity
     */
    Class<? extends Granularity<?, ?>> granularity();

    /**
     * Options of the operation.
     * <p>
     * Options are key value paris defined within {@link Option} annotation. These options are used to populate
     * the options data object of the operations. See the documentation of the operation for more information of the
     * available options each operation has.
     * <p>
     * Options are optional and do not have to be set.
     *
     * @return Array of options
     */
    Option[] options() default {};

    /**
     * Definition of {@link Function Functions}. Multiple functions can be specified.
     *
     * @return Array of function definitions
     * @see FunctionDef
     */
    FunctionDef[] functions();
}
