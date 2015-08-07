package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.AbstractDimension;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import de.schuermann.interactivedata.api.data.operations.filter.Filter.Builder;
import de.schuermann.interactivedata.api.data.operations.filter.FilterData;
import de.schuermann.interactivedata.spring.service.DataMapperService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base Class that handles request to a REST API based on a chart definition.
 *
 * @author Philipp Schürmann
 */
public abstract class AbstractChartRequestHandler<T extends AbstractChartDefinition<?, D>, D extends ChartData> {

    private Log log = LogFactory.getLog(AbstractChartRequestHandler.class);

    protected ApplicationContext applicationContext;
    protected T chartDefinition;
    protected List<Filter> filters;

    public AbstractChartRequestHandler(ApplicationContext applicationContext, T chartDefinition) {
        this.applicationContext = applicationContext;
        this.chartDefinition = chartDefinition;

        this.filters = new ArrayList<>();
        for(Object dimensionObject : chartDefinition.getDimensions()) {
            AbstractDimension dimension = (AbstractDimension) dimensionObject;
            String dataField = dimension.getDataField();
            for (Class<? extends Filter> filterClass : dimension.getFilters()) {
                Builder<?, ?> builder = Builder.getInstance(filterClass);
                builder.setFieldName(dataField);
                filters.add(builder.build());
            }
        }
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    public final D apply() {
        // TODO

        Map<String, String[]> parameters = new HashMap<>();

        // Extract filters from parameters
        List<Filter> filters = extractFilter(parameters);

        ChartData chartData = getData(chartDefinition, filters);

        D specificChartData = convertData(chartData);

        return chartDefinition.getChartPostProcessor().process(specificChartData);
    }

    /**
     * Extract Filters with FilterData from the request parameters.
     *
     * @param parameters {@Link MultivaluedMap} of the Parameters.
     * @return List of {@Link Filter Filters} containing {@Link FilterData}
     */
    protected List<Filter> extractFilter(Map<String, String[]> parameters) {
        List<Filter> requestFilters = new ArrayList<>();
        for(Filter filter : filters) {
            Class<?> filterDataType = GenericTypeResolver.resolveTypeArgument(filter.getClass(), Filter.class);
            try {
                FilterData filterData = (FilterData) getDataMapperService().mapDataOnObject(filterDataType, parameters);
                Filter requestFilter = filter;
                filter.setFilterData(filterData);
                log.debug("Extracted Filter[" + filter + "] with FilterData[" + filterData + "]");
                requestFilters.add(requestFilter);
            } catch (IllegalArgumentException e) {
                log.info("Unable to map query parameters to data object ["  + filterDataType.getName() + "] using null object, " + e.getMessage());
                requestFilters.add(filter);
            }
        }
        return requestFilters;
    }

    /**
     * Get the DataSource used for accessing data.
     *
     * @return Instance of the DataSource
     */
    protected DataSource getDataSource() {
        return (DataSource) applicationContext.getBean(chartDefinition.getDataSource());
    }

    private DataMapperService getDataMapperService() {
        return applicationContext.getBean(DataMapperService.class);
    }

    protected ChartData getData(AbstractChartDefinition chartDefinition, List<Filter> filter) {
        return getDataSource().getData(chartDefinition, filter);
    }

    /**
     * Convert Data from abstract to the specialized form. Overwrite this method to provide the special
     * data format of the chart type.
     *
     * @param chartData Abstract ChartData
     * @return Data of the chart type
     */
    protected abstract D convertData(ChartData chartData);

}
