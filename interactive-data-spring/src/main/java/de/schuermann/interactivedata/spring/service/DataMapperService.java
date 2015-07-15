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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
@Service
public class DataMapperService {

    private static Log log = LogFactory.getLog(DataMapperService.class);

    private ObjectMapper objectMapper;

    @Autowired
    public DataMapperService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T mapDataOnObject(Class<T> objectClass, MultivaluedMap<String, String> data) {
        T result = null;
        try {
            Constructor<T> constructor = objectClass.getConstructor();
            result = constructor.newInstance();

            PropertyAccessor propertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(result);

            for(Map.Entry<String, List<String>> entry : data.entrySet()) {
                String fieldName = entry.getKey();
                if(propertyAccessor.isWritableProperty(fieldName)) {
                    Object propertyValue = getPropertyValue(propertyAccessor.getPropertyType(fieldName), data.get(fieldName));
                    propertyAccessor.setPropertyValue(fieldName, propertyValue);
                }
            }
        } catch (NoSuchMethodException e) {
            log.warn("Cannot Map Data to Class [" + objectClass.getName() + "], empty Constructor missing");
        } catch (InstantiationException e) {
            log.warn("Cannot Map Data to Class [" + objectClass.getName() + "], Object cannot be instantiated, " + e.getMessage());
        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            log.warn("Cannot Map Data to Class [" + objectClass.getName() + "], " + e.getMessage());
        }
        return result;
    }

    private <T> T getPropertyValue(Class<T> propertyType, List<String> data) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        if(data == null || data.size() == 0) {
            return null;
        } else if(data.size() == 1) {
            return objectMapper.readValue(data.get(0), propertyType);
        } else {
            // TODO
            ParameterizedType parameterizedType = (ParameterizedType) propertyType.getGenericSuperclass();
            for(Type aType : parameterizedType.getActualTypeArguments()) {
                log.debug("Parameterized Types: " + aType.getTypeName());
            }
            if(propertyType.isAssignableFrom(Collection.class)) {
                Collection collection = (Collection) propertyType.getConstructor().newInstance();
                for(String value : data) {
                    collection.add(getPropertyValue(Object.class, Collections.singletonList(value)));
                }
                return propertyType.cast(collection);
            } else {
                log.warn("Collection has to be provided but property has type " + propertyType.getName());
                throw new IOException("Collection has to be provided but property has type " + propertyType.getName());
            }
        }
    }

}
