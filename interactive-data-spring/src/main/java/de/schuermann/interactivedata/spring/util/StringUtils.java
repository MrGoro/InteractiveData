package de.schuermann.interactivedata.spring.util;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public class StringUtils {

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
