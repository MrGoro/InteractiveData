package com.github.mrgoro.interactivedata.api.data.operations.filter;

import com.github.mrgoro.interactivedata.api.data.operations.EmptyOperationData;
import com.github.mrgoro.interactivedata.api.data.operations.OperationData;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;

/**
 * Filter that searches for equality of the data and the parameter.
 *
 * <blockquote>
 *     <b>Request Data:</b><br>
 *     search - text to search for <br>
 *     invert - true/false (optional, default: false)
 * </blockquote>
 * <blockquote>
 *     <b>Options:</b><br>
 *     none
 * </blockquote>
 *
 * @author Philipp Sch&uuml;rmann
 */
public class SearchFilter extends Filter<SearchFilter.SearchFilterData, EmptyOperationData> {

    public SearchFilter(String fieldName, Class fieldClass, SearchFilterData requestData, EmptyOperationData options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        return getProperty(dataObject).equals(getRequestData().getSearch()) ^ getRequestData().isInvert();
    }

    /**
     * Specification of request parameters for {@link SearchFilter}.
     *
     * @see SearchFilter
     */
    public static class SearchFilterData implements OperationData {

        private String search;
        private boolean invert = false;

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

        @Override
        public String toString() {
            return "SearchFilterData{" +
                    "search='" + search + '\'' +
                    ", invert=" + invert +
                    '}';
        }
    }
}