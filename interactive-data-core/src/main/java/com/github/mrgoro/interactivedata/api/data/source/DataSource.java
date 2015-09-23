package com.github.mrgoro.interactivedata.api.data.source;

import com.github.mrgoro.interactivedata.api.data.DataRequest;
import com.github.mrgoro.interactivedata.api.data.bean.DataObject;

import java.util.List;

/**
 * Definition of a DataSource that can be used to supply a Chart with Data.
 *
 * @author Philipp Sch&uuml;rmann
 */
public interface DataSource {

    List<DataObject> getData(DataRequest dataRequest);
}
