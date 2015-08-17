package de.schuermann.interactivedata.api.chart.definitions;

import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.functions.Function;
import de.schuermann.interactivedata.api.data.operations.granularity.Granularity;

import java.util.List;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractDimension {

    private String dataField;
    private Class dataType;

    private List<Class<? extends Filter<?>>> filters;
    private List<Class<? extends Granularity<?>>> granularities;
    private List<Class<? extends Function<?>>> functions;

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

    public List<Class<? extends Filter<?>>> getFilters() {
        return filters;
    }

    public void setFilters(List<Class<? extends Filter<?>>> filters) {
        this.filters = filters;
    }

    public List<Class<? extends Granularity<?>>> getGranularities() {
        return granularities;
    }

    public void setGranularities(List<Class<? extends Granularity<?>>> granularities) {
        this.granularities = granularities;
    }

    public List<Class<? extends Function<?>>> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Class<? extends Function<?>>> functions) {
        this.functions = functions;
    }
}
