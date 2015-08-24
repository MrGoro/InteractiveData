package de.schuermann.interactivedata.api.service;

import java.util.Map;

/**
 * Service for Mapping Data to Objects.
 *
 * @author Philipp Schürmann
 */
public interface DataMapperService {

    /**
     * Convert a {@Link Map Map<String, String[]>} (e.g. used for Request Parameters) to a Java POJO.
     *
     * @param objectClass Class of the destination Object
     * @param data Data to fill the Object with
     * @param <T> Class of the destination Object
     * @return POJO
     */
    <T> T mapMultiDataOnObject(Map<String, String[]> data, Class<T> objectClass);

    /**
     * Convert a {@Link Map Map<String, String>} (e.g. used for Options of key value pairs) to a Java POJO.
     *
     * @param objectClass Class of the destination Object
     * @param data Data to fill the Object with
     * @param <T> Class of the destination Object
     * @return POJO
     */
    <T> T mapDataOnObject(Map<String, ?> data, Class<T> objectClass);
}
