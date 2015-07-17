package de.schuermann.interactivedata.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Philipp Schürmann
 */
@Service
public class DataMapperService {

    private ObjectMapper objectMapper;

    @Autowired
    public DataMapperService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Convert a {@Link MultivaluedMap MultivalueMap} (e.g. used for Request Parameters) to a Java POJO.
     *
     * @param objectClass Class of the destination Object
     * @param data Data to fill the Object with
     * @param <T> Class of the destination Object
     * @return POJO
     */
    public <T> T mapDataOnObject(Class<T> objectClass, MultivaluedMap<String, String> data) throws IllegalArgumentException {
        return objectMapper.convertValue(getAsMap(data), objectClass);
    }

    /**
     * Convert a {@Link MultivaluedMap MultivalueMap} to a {@Link Map Map<String, Object>}.
     *
     * Replaces {@Link List Lists} with only one Object by the object itself.
     *
     * @param multivaluedMap {@Link MultivaluedMap MultivalueMap} to convert
     * @return {@Link Map Map<String, Object>}
     */
    private Map<String, Object> getAsMap(MultivaluedMap<String, String> multivaluedMap) {
        Map<String, Object> map = new HashMap<>();
        for(Map.Entry<String, List<String>> entry : multivaluedMap.entrySet()) {
            List<String> value = entry.getValue();
            if(value != null) {
                if (value.size() > 1) {
                    map.put(entry.getKey(), value);
                } else {
                    map.put(entry.getKey(), value.get(0));
                }
            }
        }
        return map;
    }

}
