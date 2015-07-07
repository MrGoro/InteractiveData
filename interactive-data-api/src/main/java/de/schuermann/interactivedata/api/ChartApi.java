package de.schuermann.interactivedata.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating the root of a collection of corresponding APIs.
 *
 * @author Philipp Schürmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ChartApi {

    /**
     * Path (URL) the API will be available at.
     *
     * @return Path String
     */
    String name();

}
