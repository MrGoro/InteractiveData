package de.schuermann.interactivedata.api.chart.annotations;

import de.schuermann.interactivedata.api.data.DataSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Philipp Schürmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Chart {

    String value();

    Class<? extends DataSource> dataSource();

    FilterDef[] filter() default {};

    OperationDef[] operations() default {};
}
