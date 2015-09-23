package com.github.mrgoro.interactivedata.api.chart.definitions;

/**
 * Generic definition of a dimension of a chart.
 * Dimensions specify the data to extract for visualization.
 *
 * @author Philipp Sch&uuml;rmann
 */
public abstract class AbstractDimension {

    private String dataField;
    private Class dataType;

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

}
