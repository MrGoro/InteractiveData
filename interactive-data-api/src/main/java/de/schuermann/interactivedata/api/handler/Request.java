package de.schuermann.interactivedata.api.handler;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;

import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class Request {

    public String name;
    private List<Filter> filter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Filter> getFilter() {
        return filter;
    }

    public void setFilter(List<Filter> filter) {
        this.filter = filter;
    }
}
