package de.schuermann.interactivedata.spring.rest;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.filter.TimeFilterData;
import de.schuermann.interactivedata.spring.service.DataMapperService;
import de.schuermann.interactivedata.spring.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.process.Inflector;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collection;
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

    public AbstractRequestHandler(ApplicationContext applicationContext, T chartDefinition) {
        this.applicationContext = applicationContext;
        this.chartDefinition = chartDefinition;
    }

    protected T getChartDefinition() {
        return chartDefinition;
    }

    @Override
    public D apply(ContainerRequestContext containerRequestContext) {
        extract(containerRequestContext);
        return getData();
    }

    protected void extract(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> pathParameters = containerRequestContext.getUriInfo().getPathParameters();
        log.debug("Path-Parameters: " + StringUtils.multivaluedMapToString(pathParameters));
        MultivaluedMap<String, String> queryParameters = containerRequestContext.getUriInfo().getQueryParameters();
        log.debug("Query-Parameters: " + StringUtils.multivaluedMapToString(queryParameters));

        DataMapperService dataMapperService = getDataMapperService();
        TimeFilterData timeFilterData = dataMapperService.mapDataOnObject(TimeFilterData.class, queryParameters);
        log.debug(timeFilterData);
    }

    protected DataSource getDataSource() {
        return (DataSource) applicationContext.getBean(chartDefinition.getDataSource());
    }

    private DataMapperService getDataMapperService() {
        return applicationContext.getBean(DataMapperService.class);
    }

    protected abstract D getData();


}
