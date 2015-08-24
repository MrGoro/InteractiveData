package de.schuermann.interactivedata.api.chart.annotations;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;

/**
 * Definition of a {@link Filter} that is used to filter out data at chart generation.
 *
 * @author Philipp Sch&uuml;rmann
 */
public @interface FilterDef {

    /**
     * Name of the field the filter operates on.
     *
     * @return Name of the field
     */
    String fieldName();

    /**
     * Type of the field the filter operates on.
     * <p>
     * Note that {@link Filter Fiters} do not support every type. See the documentation of the filter for
     * more information about data types supported.
     *
     * @return Type of the field
     */
    Class<?> fieldClass();

    /**
     * Class of the Filter. Filter class must extend {@link Filter}.
     *
     * @return Class of the Filter.
     */
    Class<? extends Filter<?, ?>> filter();

    /**
     * Options of the filters.
     * <p>
     * Options are key value paris defined within {@link Option} annotation. These options are used to populate
     * the options data object of the filter. See the documentation of the filter for more information of the
     * available options each filter has.
     * <p>
     * Options are optional and do not have to be set.
     *
     * @return Array of options
     */
    Option[] options() default {};
}
