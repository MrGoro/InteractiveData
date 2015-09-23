package com.github.mrgoro.interactivedata.api.data.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class ReflectiveDataObject extends MapDataObject {

    private static final Log log = LogFactory.getLog(ReflectiveDataObject.class);

    private Object origin;
    private PropertyDescriptor[] propertyDescriptors;

    ReflectiveDataObject(Object origin) {
        super();
        this.origin = origin;
        try {
            this.propertyDescriptors = Introspector.getBeanInfo(origin.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            log.warn("Error during DataObject creation for Object of class: [" + origin.getClass() + "], " + e.getMessage(), e);
        }
    }

    @Override
    public Object getProperty(String name) throws NoSuchFieldException {
        if(propertyDescriptors != null) {
            try {
                for (PropertyDescriptor pd : propertyDescriptors) {
                    if (pd.getReadMethod() != null && pd.getName().equals(name) && !"class".equals(pd.getName())) {
                        return pd.getReadMethod().invoke(origin);
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.debug("Error reading property [" + name + " of DataObject of class: [" + origin.getClass() + "], " + e.getMessage(), e);
            }
        }
        return super.getProperty(name);
    }

    @Override
    public void setProperty(String name, Object value) {
        if (propertyDescriptors != null) {
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
        }
        super.setProperty(name, value);
    }

    @Override
    public Map<String, Object> getAsMap() {
        Map<String, Object> map = super.getAsMap();
        if (propertyDescriptors != null) {
            try {
                for (PropertyDescriptor pd : propertyDescriptors) {
                    if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                        map.put(pd.getName(), pd.getReadMethod().invoke(origin));
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.debug("Error creating Map from DataObject: [" + origin.getClass().getSimpleName() + "], " + e.getMessage(), e);
            }
        }
        return map;
    }
}