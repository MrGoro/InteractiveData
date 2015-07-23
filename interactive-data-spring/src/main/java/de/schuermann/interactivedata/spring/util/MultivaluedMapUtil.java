package de.schuermann.interactivedata.spring.util;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class MultivaluedMapUtil {

    /**
     * Create a new {@Link MultivaluedMap MultivaluedMap} containing all elements of the given 2 {@Link MultivaluedMap MultivaluedMap}
     *
     * @param map1 First {@Link MultivaluedMap MultivaluedMap}
     * @param map2 Second {@Link MultivaluedMap MultivaluedMap}
     * @param <K> Type of the Key of all {@Link MultivaluedMap MultivaluedMaps}
     * @param <V> Type of the Value {@Link MultivaluedMap MultivaluedMaps}
     * @return New {@Link MultivaluedHashMap MultivaluedHashMap} containing all elements of the given 2 {@Link MultivaluedMap MultivaluedMaps}
     */
    public static <K, V> MultivaluedMap<K, V> combine(MultivaluedMap<K, V> map1, MultivaluedMap<K, V> map2) {
        MultivaluedMap<K, V> result = new MultivaluedHashMap<>();
        map1.forEach(result::put);
        map2.forEach(result::put);
        return result;
    }

    /**
     * Convert a MultivaluedMap to a readable String.
     *
     * @param multivaluedMap MultivaluedMap to convert
     * @return String containing all Keys and Values of the MultivaluedMap
     */
    public static String multivaluedMapToString(MultivaluedMap<String, String> multivaluedMap) {
        String result = "";
        for(Map.Entry<String, List<String>> entry : multivaluedMap.entrySet()) {
            String valueString = "";
            for(String value : entry.getValue()) {
                valueString += value + " , ";
            }
            if(valueString.endsWith(" , ")) {
                valueString = valueString.substring(0, valueString.length()-2);
            }
            result += entry.getKey() + " [ " + valueString + "] ";
        }
        return result.trim();
    }
}
