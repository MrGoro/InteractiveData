package de.schuermann.interactivedata.api.chart.types;

import java.lang.annotation.*;

/**
 * @author Philipp Schürmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Chart {

    String value();

}
