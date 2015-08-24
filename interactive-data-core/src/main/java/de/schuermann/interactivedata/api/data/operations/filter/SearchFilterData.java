package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.RequestData;

/**
 * @author Philipp Schürmann
 */
public class SearchFilterData implements RequestData {

    private String search;
    private boolean invert;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    @Override
    public boolean hasData() {
        return search != null && !search.isEmpty();
    }
}
