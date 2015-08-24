package de.schuermann.interactivedata.api.chart.annotations;

/**
 * Definition of one option.
 * <p>
 * An option is a pair of key and value. The key specifies the field of the object, the value specifies its value.
 *
 * @author Philipp Sch&uuml;rmann
 */
public @interface Option {

    /**
     * Key specifying the field of the object to map on.
     *
     * @return Key
     */
    String key();

    /**
     * Value specifying the value that should be mapped onto the field specified in the key.
     *
     * @return Value
     */
    String value();
}
