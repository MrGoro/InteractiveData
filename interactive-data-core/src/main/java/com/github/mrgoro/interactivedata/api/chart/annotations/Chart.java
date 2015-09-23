package com.github.mrgoro.interactivedata.api.chart.annotations;

import com.github.mrgoro.interactivedata.api.chart.data.ChartData;
import com.github.mrgoro.interactivedata.api.data.operations.Operation;
import com.github.mrgoro.interactivedata.api.data.operations.filter.Filter;
import com.github.mrgoro.interactivedata.api.data.source.DataSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base definition of a chart to generate an api.
 * <p>
 * This definition is always used together with the specific definition of the chart type. Methods must always
 * be annotated with both.
 * <p>
 * If the annotated method has a return type and exactly of parameter of type
 * {@link ChartData} the method is used for post processing. It will
 * get called after the frameworks completes generation of the response data. This can be used to do further
 * optimization for special cases that are only needed once.
 * <p>
 * {@link Filter Filters} are used to filter out data that should not be used for chart generation.
 * {@link Operation Operations} combine a granularity and multiple functions that are used to reduce that data by grouping.
 *
 * <pre>
 *  <code>
 *      {@literal @}Chart(
 *          name = "mychart",
 *          dataSource = MyDbDataSource.class,
 *          filter = {
 *              {@literal @}FilterDef(fieldName = "time", fieldClass = Date.class, filter = TimeFilter.class)
 *          },
 *          operations = {
 *              {@literal @}OperationDef(
 *                  fieldName = "user",
 *                  fieldClass = User.class,
 *                  granularity = DistinctGranularity.class,
 *                  functions = {
 *                      {@literal @}FunctionDef(
 *                          fieldName = "id",
 *                          fieldClass = Long.class,
 *                          function = Count.class
 *                      )
 *                  }
 *              )
 *          }
 *      )
 *  </code>
 * </pre>
 *
 * @author Philipp Sch&uuml;rmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Chart {

    /**
     * Name of the chart.
     * <p>
     * The name is part of the identification pattern for the api. The identification of a chart api has the format
     * of %Service-Name%/%Chart-Name%.
     *
     * @return Name of the chart.
     */
    String name();

    /**
     * Data source to use for querying data.
     *
     * @return Class of the data source
     */
    Class<? extends DataSource> dataSource();

    /**
     * Definitions of Filters. You can specify none, one ore more filters.
     * Filters are executed before operations.
     *
     * @return Array of Operations
     */
    FilterDef[] filter() default {};

    /**
     * Definitions of Operations. You can specify none, one ore more operations.
     * Operations are executed after filters.
     *
     * @return Array of Operations
     * @see OperationDef
     * @see FunctionDef
     */
    OperationDef[] operations() default {};
}
