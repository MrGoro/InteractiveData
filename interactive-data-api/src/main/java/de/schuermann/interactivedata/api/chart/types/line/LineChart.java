package de.schuermann.interactivedata.api.chart.types.line;

import de.schuermann.interactivedata.api.data.DataSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Philipp Sch�rmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LineChart {

    Class<? extends DataSource> dataSource();

    Axis[] axis();

}
