package de.schuermann.interactivedata.api.handler;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Object for wrapping information about a specific request for chart data.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class Request {

    public String name;
    public Map<String, String[]> data;

    public Request(String name) {
        this.name = name;
    }

    public Request(String name, Map<String, String[]> data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String[]> getData() {
        return data;
    }

    public void setData(Map<String, String[]> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", data={" + stringifyData() + "}" +
                '}';
    }

    private String stringifyData() {
        return data.entrySet().stream()
                .map(entry -> entry.getKey() + "->" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
