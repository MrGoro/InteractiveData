package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.chart.definitions.AbstractDimension;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;
import de.schuermann.interactivedata.spring.service.DataMapperService;
import de.schuermann.interactivedata.spring.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.process.Inflector;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philipp Schürmann
 */
public abstract class AbstractRequestHandler<T extends AbstractChartDefinition, D extends ChartData> implements Inflector<ContainerRequestContext, ChartData> {

    private Log log = LogFactory.getLog(AbstractRequestHandler.class);

    protected ApplicationContext applicationContext;
    protected T chartDefinition;
    protected List<Filter> filters;

    public AbstractRequestHandler(ApplicationContext applicationContext, T chartDefinition) {
        this.applicationContext = applicationContext;
        this.chartDefinition = chartDefinition;

        this.filters = new ArrayList<>();
        for(Object dimensionObject : chartDefinition.getDimensions()) {
            AbstractDimension dimension = (AbstractDimension) dimensionObject;
            String dataField = dimension.getDataField();
            for (Class<? extends Filter> filterClass : dimension.getFilters()) {
                try {
                    Filter filter = filterClass.newInstance();
                    filter.setFieldName(dataField);
                    filters.add(filter);
                } catch (IllegalAccessException | InstantiationException e) {
                    log.warn("Error initializing filter [" + filterClass.getName() + "], " + e.getMessage());
                }
            }
        }
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    @Override
    public D apply(ContainerRequestContext containerRequestContext) {
        return convertData(getData(chartDefinition, extractFilter(containerRequestContext)));
    }

    protected List<Filter> extractFilter(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> pathParameters = containerRequestContext.getUriInfo().getPathParameters();
        log.debug("Path-Parameters: " + StringUtils.multivaluedMapToString(pathParameters));
        MultivaluedMap<String, String> queryParameters = containerRequestContext.getUriInfo().getQueryParameters();
        log.debug("Query-Parameters: " + StringUtils.multivaluedMapToString(queryParameters));

        List<Filter> requestFilters = new ArrayList<>();
        for(Filter filter : filters) {
            Class<?> filterDataType = GenericTypeResolver.resolveTypeArgument(filter.getClass(), Filter.class);
            try {
                FilterData filterData = (FilterData) getDataMapperService().mapDataOnObject(filterDataType, queryParameters);

                Filter requestFilter = filter. clone();
                filter.setFilterData(filterData);
                requestFilters.add(requestFilter);
            } catch (IllegalArgumentException e) {
                log.info("Unable to map query parameters to data object ["  + filterDataType.getName() + "] using null object, " + e.getMessage());
                requestFilters.add(filter);
            } catch (CloneNotSupportedException e) {
                log.warn("Filter [" + filter.getClass().getName() + "] not cloneable but required for using filter data, " + e.getMessage());
                requestFilters.add(filter);
            }
        }
        return requestFilters;
    }

    protected DataSource getDataSource() {
        return (DataSource) applicationContext.getBean(chartDefinition.getDataSource());
    }

    private DataMapperService getDataMapperService() {
        return applicationContext.getBean(DataMapperService.class);
    }

    protected ChartData getData(AbstractChartDefinition chartDefinition, List<Filter> filter) {
        return getDataSource().getData(chartDefinition, filter);
    }

    protected abstract D convertData(ChartData chartData);

}
