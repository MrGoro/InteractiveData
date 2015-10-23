package com.github.mrgoro.interactivedata.api.data.operations.filter;

import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.operations.EmptyOperationData;
import com.github.mrgoro.interactivedata.api.data.operations.OperationData;

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

    private Object searchObject;

    public SearchFilter(String fieldName, Class fieldClass, SearchFilterData requestData, EmptyOperationData options) {
        super(fieldName, fieldClass, requestData, options);
        // Cache Search Object
        if(fieldName != null && fieldClass != null && requestData != null && options != null && requestData.hasData()) {
            searchObject = getSearch(getFieldClass());
        }
    }

    private <T> T getSearch(Class<T> type) {
        String search = getRequestData().getSearch();
        Object typed = search;
        if(type.equals(Long.class)) {
            typed = Long.valueOf(search);
        } else if(type.equals(Integer.class)) {
            typed = Integer.valueOf(search);
        }
        return type.cast(typed);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        return getProperty(dataObject).equals(searchObject) ^ getRequestData().isInvert();
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