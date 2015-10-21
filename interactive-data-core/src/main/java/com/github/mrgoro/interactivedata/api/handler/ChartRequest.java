package com.github.mrgoro.interactivedata.api.handler;

import com.github.mrgoro.interactivedata.api.util.Utils;

import java.util.Map;

/**
 * Data Object for wrapping information about a specific request for chart data.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class ChartRequest {

    public String name;
    public Map<String, String[]> data;

    public ChartRequest(String name) {
        this.name = name;
    }

    public ChartRequest(String name, Map<String, String[]> data) {
        this.name = name;
        this.data = data;
    }
// getter and setter
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
        return "ChartRequest{" +
                "name='" + name + '\'' +
                ", data={" + Utils.stringify(data) + "}" +
                '}';
    }
}
