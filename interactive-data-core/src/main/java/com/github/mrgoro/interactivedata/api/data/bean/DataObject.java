package com.github.mrgoro.interactivedata.api.data.bean;

import java.util.Map;
import java.util.Optional;

/**
 * Data Access Object for easy accessing data in objects with keys and values.
 * <p>
 * The easiest form of a DataObject is a wrapper around a {@code Map<String, Object>}.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface DataObject {

    /**
     * Get a property by its name.
     *
     * @param name Name of the property
     * @return Value of the property
     * @throws NoSuchFieldException When property is non existent
     */
    Object getProperty(String name) throws NoSuchFieldException;

    /**
     * Get a property by its name.
     * <p>
     * {@link Optional} is empty when property is non existent.
     *
     * @param name Name of the property
     * @return Optional containing the value of the property, or empty when property is non existent
     */
    default Optional<Object> getOptionalProperty(String name) {
        try {
            return Optional.of(getProperty(name));
        } catch (NoSuchFieldException nsfe) {
            return Optional.empty();
        }
    }

    /**
     * Get a property by its name with the correct type.
     * <p>
     * The Method explicitly casts the property's value to the specified type. Throws {@link ClassCastException} when
     * casting fails.
     *
     * @param name Name of the property
     * @param type Type of the property
     * @param <T>  Type of the property
     * @return Value of the property
     * @throws NoSuchFieldException When property is non existent
     * @throws ClassCastException   if the object is not null and is not assignable to the type T
     */
    default <T> T getProperty(String name, Class<T> type) throws NoSuchFieldException {
        return type.cast(getProperty(name));
    }

    /**
     * Get a property by its name with the correct type.
     * <p>
     * {@link Optional} is empty when property is non existent.
     * <p>
     * The Method explicitly casts the property's value to the specified type. Throws {@link ClassCastException} when
     * casting fails.
     *
     * @param name Name of the property
     * @param type Type of the property
     * @param <T>  Type of the property
     * @return Optional containing the value of the property, or empty when property is non existent
     * @throws ClassCastException if the object is not null and is not assignable to the type T
     */
    default <T> Optional<T> getOptionalProperty(String name, Class<T> type) {
        try {
            return Optional.of(getProperty(name, type));
        } catch (NoSuchFieldException nsfe) {
            return Optional.empty();
        }
    }

    /**
     * Set a property by its name.
     *
     * @param name  Name of the property
     * @param value Value of the property
     */
    void setProperty(String name, Object value);

    /**
     * Transform the DataObject to a {@link Map} having property names as keys and values as the map's values.
     *
     * @return Map of properties
     */
    Map<String, Object> getAsMap();

    /**
     * Set a property by its name and return the DataObject.
     *
     * @param name  Name of the property
     * @param value Value of the property
     * @see #setProperty(String, Object)
     * @return DataObject
     */
    DataObject setPropertyAndGet(String name, Object value);
}