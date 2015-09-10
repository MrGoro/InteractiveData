package de.schuermann.interactivedata.api.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class providing static helper methods for all types of use cases.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class Utils {

    /**
     * Convert a multi value map (String array as values) to a String of format:
     * <pre>
     *     KEY1->[VALUE1,VALUE2,VALUE3], KEY2->[VALUE1]
     * </pre>
     *
     * @param map Map to stringify
     * @return Stringyfied Map
     */
    public static String stringify(Map<String, String[]> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + "->" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
