package de.schuermann.interactivedata.api.data;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;
import de.schuermann.interactivedata.api.functions.Function;
import de.schuermann.interactivedata.api.granularity.Granularity;

import java.util.List;
import java.util.Map;

/**
 * Definition of a DataSource that can be used to supply a Chart with Data.
 *
 * @author Philipp Schürmann
 */
public interface DataSource {

    ChartData getData(AbstractChartDefinition chartDefinition, List<Filter> filters);

}
