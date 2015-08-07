package de.schuermann.interactivedata.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Convert a {@Link Map Map<String, String[]>} (e.g. used for Request Parameters) to a Java POJO.
     *
     * @param objectClass Class of the destination Object
     * @param data Data to fill the Object with
     * @param <T> Class of the destination Object
     * @return POJO
     */
    public <T> T mapDataOnObject(Class<T> objectClass, Map<String, String[]> data) throws IllegalArgumentException {
        return objectMapper.convertValue(getAsMap(data), objectClass);
    }

    /**
     * Convert a {@Link Map Map<String, String[]>} to a {@Link Map Map<String, Object>}.
     *
     * Replaces {@Link List Lists} with only one Object by the object itself.
     *
     * @param stringMap {@Link Map Map<String, String[]>} to convert
     * @return {@Link Map Map<String, Object>}
     */
    private Map<String, Object> getAsMap(Map<String, String[]> stringMap) {
        Map<String, Object> map = new HashMap<>();
        for(Map.Entry<String, String[]> entry : stringMap.entrySet()) {
            List<String> value = Arrays.asList(entry.getValue());
            if (value.size() > 1) {
                map.put(entry.getKey(), value);
            } else {
                map.put(entry.getKey(), value.get(0));
            }
        }
        return map;
    }

}
