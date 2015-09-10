package de.schuermann.interactivedata.api.data.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class MapDataObject implements DataObject {

    private Map<String, Object> properties = new HashMap<>();

    MapDataObject() {}

    MapDataObject(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public Object getProperty(String name) throws NoSuchFieldException {
        if(properties.containsKey(name)) {
            return properties.get(name);
        } else {
            throw new NoSuchFieldException("No property with name " + name);
        }
    }

    @Override
    public void setProperty(String name, Object value) {
        this.properties.put(name, value);
    }

    @Override
    public Map<String, Object> getAsMap() {
        return properties;
    }
}
