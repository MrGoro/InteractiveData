package de.schuermann.interactivedata.api.data.operations;

/**
 * Placeholder class specifying that the {@link Operation} does not need to be parameterized.
 * <p>
 * Note: The {@link #hasData()} method always return {@code true} as there is no possibility to derive
 * this information from zero data. Therefore the Operations always operates - which should always be the case.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class EmptyOperationData implements OperationData {

    @Override
    public boolean hasData() {
        return true;
    }

    @Override
    public String toString() {
        return "Empty{}";
    }
}
