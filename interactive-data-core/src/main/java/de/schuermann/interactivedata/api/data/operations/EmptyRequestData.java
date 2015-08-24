package de.schuermann.interactivedata.api.data.operations;

/**
 * @author Philipp Schürmann
 */
public class EmptyRequestData implements RequestData {

    @Override
    public boolean hasData() {
        return true;
    }
}
