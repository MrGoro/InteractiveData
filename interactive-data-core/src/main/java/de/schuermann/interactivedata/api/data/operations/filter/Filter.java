package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.OperationData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.function.Predicate;

/**
 * Specification of a Filter.
 * <p>
 * A filter is capable of checking if a given object should be included or excluded for the given request.
 * <p>
 * Filters are parameterized with request data and options. Options are set once. Request data originates from
 * a specific request and is therefore set per request. The Framework automatically populates the data for options
 * and requests. Filters only have to specify the form of the data with custom {@link OperationData} classes.
 *
 * @param <D> Type of the Request Data Object
 * @param <O> Type of the Options Object
 * @author Philipp Sch&uuml;rman
 */
public abstract class Filter<D extends OperationData, O extends OperationData> extends Operation<D, O> {

    /**
     * Test if the given {@link DataObject} should be included or excluded.
     *
     * @param dataObject DataObject to test
     * @return true if object has to be included, false if not
     */
    protected abstract boolean test(DataObject dataObject);

    /**
     * Get a {@link Predicate} from the filter, capable of checking if a {@link DataObject} should be filtered out.
     *
     * @return Predicate for filtering
     */
    public Predicate<? super DataObject> toPredicate() {
        return this::test;
    }

    public Filter(String fieldName, Class fieldClass, D requestData, O options) {
        super(fieldName, fieldClass, requestData, options);
    }
}
