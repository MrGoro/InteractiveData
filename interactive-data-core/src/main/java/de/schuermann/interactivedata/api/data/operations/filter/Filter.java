package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.Operation;
import de.schuermann.interactivedata.api.data.operations.RequestData;
import de.schuermann.interactivedata.api.data.reflection.DataObject;
import de.schuermann.interactivedata.api.service.annotations.FilterService;

import java.util.function.Predicate;

/**
 * Base for every Filter. Used for storing information of the filter that cannot be accessed / changed
 * from a request.
 *
 * @author Philipp Schürmann
 */
@FilterService
public abstract class Filter<D extends RequestData, O extends RequestData> extends Operation<D, O> {

    public Filter(String fieldName, Class fieldClass, D requestData, O options) {
        super(fieldName, fieldClass, requestData, options);
    }

    /**
     * Get a Predicate that is capable of filtering using this filters information.
     *
     * @return Predicate
     */
    public Predicate<? super DataObject> toPredicate() {
        return new FilterPredicate(this);
    }

    /**
     * Test if the given Data object is suitable for current filter.
     *
     * @param dataObject DataObject
     * @return true if object has to be included, false if not
     */
    protected abstract boolean test(DataObject dataObject);

    private static class FilterPredicate implements Predicate<DataObject> {

        private Filter filter;

        public FilterPredicate(Filter filter) {
            this.filter = filter;
        }

        @Override
        public boolean test(DataObject t) {
            return filter.test(t);
        }
    }
}
