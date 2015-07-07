package de.schuermann.interactivedata.spring.chart;

import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.functions.Function;
import de.schuermann.interactivedata.api.granularity.Granularity;

import java.util.List;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractDimension {

    private String dataField;
    private Class dataType;

    private List<Filter> filters;
    private List<Granularity> granularities;
    private List<Function> functions;

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public Class getDataType() {
        return dataType;
    }

    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Granularity> getGranularities() {
        return granularities;
    }

    public void setGranularities(List<Granularity> granularities) {
        this.granularities = granularities;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }
}
