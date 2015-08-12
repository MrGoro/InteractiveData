package de.schuermann.interactivedata.api.data.operations.filter;

import java.io.Serializable;

/**
 * Marker Interface for identifying Data for Filters.
 *
 * FilterData is populated for every request using request parameters.
 *
 * @author Philipp Schürmann
 */
public interface FilterData extends Serializable {

    /**
     * Check if the filter data is in a state that it wants to filter.
     *
     * Every FilterData class must override this method to support the decision if the FilterData has
     * enough data for filtering or should just be skipped.
     *
     * @return true if filter data wants to filter, false if not
     */
    boolean doFilter();
}
