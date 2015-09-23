package com.github.mrgoro.interactivedata.api.data.bean;

import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class DataObjectFactory {

    /**
     * Create a new DataObject around that given object.
     * @param origin Object to access
     * @return New DataObject
     */
    public static DataObject create(Object origin) {
        return new ReflectiveDataObject(origin);
    }

    /**
     * Create a new DataObject that just uses Key Value Pairs.
     *
     * This is just a wrapper around a Map.
     *
     * @param map the Map with initial data
     * @return DataObject
     */
    public static DataObject create(Map<String, Object> map) {
        return new MapDataObject(map);
    }

    public static DataObject createEmpty() {
        return new MapDataObject();
    }
}
