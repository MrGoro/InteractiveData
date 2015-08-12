package de.schuermann.interactivedata.api.data.reflection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper Class that makes access to properties using getters easier.
 *
 * @author Philipp Schürmann
 */
public class DataObject {

    private static Log log = LogFactory.getLog(DataObject.class);

    private Object origin;
    private Map<String, Object> extraProperties = new HashMap<>();
    private PropertyDescriptor[] propertyDescriptors;

    /**
     * Create a new DataObject around that given object.
     * @param origin Object to access
     * @return New DataObject
     */
    public static DataObject create(Object origin) {
        return new DataObject(origin);
    }

    private DataObject(Object origin) {
        this.origin = origin;
        try {
            this.propertyDescriptors = Introspector.getBeanInfo(origin.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            log.warn("Error during DataObject creation for Object of class: [" + origin.getClass() + "], " + e.getMessage(), e);
        }
    }

    /**
     * Get the value of a property with the given name using getters.
     *
     * @param name Name of the property
     * @return Value of the property
     */
    public Object getProperty(String name) {
        try {
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getReadMethod() != null && pd.getName().equals(name) && !"class".equals(pd.getName())) {
                    return pd.getReadMethod().invoke(origin);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.debug("Error reading property [" + name + " of DataObject of class: [" + origin.getClass() + "], " + e.getMessage(), e);
        }
        return getExtraProperty(name);
    }

    private Object getExtraProperty(String name) {
        return extraProperties.get(name);
    }

    /**
     * Get the value of a property with the given name using getters.
     *
     * Value is casted to the specified class.
     *
     * @param name Name of the property
     * @param type Class of the property type
     * @param <T> Type of the property
     * @return Value of the property
     */
    public <T> T getProperty(String name, Class<T> type) {
        return type.cast(getProperty(name));
    }

    /**
     * Get the origin object the DataObject is wrapping.
     *
     * @return Origin Object
     */
    public Object getOrigin() {
        return origin;
    }

    public void setProperty(String name, Object value) {
        try {
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getWriteMethod() != null && pd.getName().equals(name) && !"class".equals(pd.getName())) {
                    pd.getWriteMethod().invoke(origin);
                    return;
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.debug("Error writing property [" + name + " of DataObject of class: [" + origin.getClass() + "], " + e.getMessage(), e);
        }
        setExtraProperty(name, value);
    }

    private void setExtraProperty(String name, Object value) {
        extraProperties.put(name, value);
    }
}
