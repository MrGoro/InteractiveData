package de.schuermann.interactivedata.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.schuermann.interactivedata.api.service.DataMapperService;
import de.schuermann.interactivedata.api.util.exceptions.RequestDataException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class JacksonDataMapperService implements DataMapperService {

    private static Log log = LogFactory.getLog(JacksonDataMapperService.class);

    private ObjectMapper objectMapper;

    @Autowired
    public JacksonDataMapperService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T mapMultiDataOnObject(Map<String, String[]> data, Class<T> objectClass) {
        return mapDataOnObject(getAsMap(data), objectClass);
    }

    @Override
    public <T> T mapDataOnObject(Map<String, ?> data, Class<T> objectClass) {
        try {
            return objectMapper.convertValue(data, objectClass);
        } catch (IllegalArgumentException e) {
            throw new RequestDataException("Parameter has wrong form and cannot be mapped on data class", e);
        }
    }

    /**
     * Convert a {@Link Map Map<String, String[]>} to a {@Link Map Map<String, Object>}.
     *
     * Replaces {@Link List Lists} with only one Object by the object itself.
     *
     * @param stringMap {@Link Map Map<String, String[]>} to convertData
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
