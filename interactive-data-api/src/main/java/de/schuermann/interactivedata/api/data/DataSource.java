package de.schuermann.interactivedata.api.data;

import de.schuermann.interactivedata.api.data.reflection.DataObject;

import java.util.List;

/**
 * Definition of a DataSource that can be used to supply a Chart with Data.
 *
 * @author Philipp Schürmann
 */
public interface DataSource {

    List<DataObject> getData(DataRequest dataRequest);
}
