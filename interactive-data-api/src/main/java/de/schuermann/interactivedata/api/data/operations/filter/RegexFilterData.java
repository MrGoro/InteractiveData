package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.RequestData;

/**
 * @author Philipp Schürmann
 */
public class RegexFilterData implements RequestData {

    private String regex;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean hasData() {
        return regex != null && !regex.isEmpty();
    }
}
