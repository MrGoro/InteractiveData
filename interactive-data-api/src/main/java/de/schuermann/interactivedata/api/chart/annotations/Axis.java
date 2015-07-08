package de.schuermann.interactivedata.api.chart.annotations;

import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.functions.Function;
import de.schuermann.interactivedata.api.granularity.Granularity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Philipp Schürmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Axis {

    enum Type { X, Y }

    Type type() default Type.X;

    String dataField();

    Class dataType() default Object.class;

    Class<? extends Granularity>[] granularity() default {};

    Class<? extends Filter>[] filter() default {};

    Class<? extends Function>[] functions() default {};
}
