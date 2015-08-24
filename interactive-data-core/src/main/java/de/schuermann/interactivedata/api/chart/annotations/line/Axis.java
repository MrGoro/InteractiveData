package de.schuermann.interactivedata.api.chart.annotations.line;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Axis {

    Type type() default Type.X;

    String dataField();

    Class dataType() default Object.class;

    Class<? extends Filter<?, ?>>[] filter() default {};

    Class<? extends Granularity<?, ?>>[] granularity() default {};

    Class<? extends Function<?, ?>>[] functions() default {};

    enum Type {X, Y}
}
