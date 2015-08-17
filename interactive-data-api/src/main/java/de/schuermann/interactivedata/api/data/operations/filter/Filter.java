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
public abstract class Filter<D extends RequestData> extends Operation<D> {

    public Filter(String fieldName, Class fieldClass, D requestData) {
        super(fieldName, fieldClass, requestData);
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
     * Checks if the filter does have data so it does want to filter.
     * If false no filtering is needed at all and should not be done.
     *
     * @return true if filter wants to filter, false if not
     */
    public boolean shouldFilter() {
        return getRequestData() != null && getRequestData().hasData();
    }

    /**
     * Test if the given Data object is suitable for current filter.
     *
     * @param t DataObject
     * @return true if object has to be included, false if not
     */
    protected abstract boolean test(DataObject t);

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
