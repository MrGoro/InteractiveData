package com.github.mrgoro.interactivedata.api.data.operations;

import java.io.Serializable;

/**
 * Data Object to specify information an {@link Operation} uses.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface OperationData extends Serializable {

    /**
     * Check if the data object has sufficient data to make it worth operating.
     * <p>
     * If this returns false the operation may be skipped.
     *
     * @return Whether the data is sufficient to operate
     */
    boolean hasData();
}
