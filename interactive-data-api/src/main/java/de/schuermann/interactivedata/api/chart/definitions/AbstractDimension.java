package de.schuermann.interactivedata.api.chart.definitions;

/**
 * @author Philipp Schürmann
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
