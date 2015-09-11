package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.handler.ChartRequestHandler;
import de.schuermann.interactivedata.api.service.ChartDefinitionService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.api.util.exceptions.RequestDataException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Service providing specific request handlers for charts by their identifiers (service name and chart name).
 *
 * If a {@link org.springframework.cache.CacheManager} is configured for the application, the result of
 * {@link #getChartRequestHandler(String, String)} is cached with the name "chartRequestHandler". This can
 * improve performance by not needing to locate, initialize and setup the {@link ChartRequestHandler} each time.
 *
 * @author Philipp Sch&uuml;rmann
 */
@Service
public class ChartRequestHandlerService {

    private static Log log = LogFactory.getLog(ChartRequestHandlerService.class);

    private ServiceProvider serviceProvider;
    private ChartDefinitionService chartDefinitionService;
    
    @Autowired
    public ChartRequestHandlerService(ServiceProvider serviceProvider, ChartDefinitionService chartDefinitionService) {
        this.serviceProvider = serviceProvider;
        this.chartDefinitionService = chartDefinitionService;
    }

    /**
     * Get a {@link ChartRequestHandler} that is capable of handling the chart with the given service and chart name.
     *
     * If a {@link org.springframework.cache.CacheManager} is configured for the application, the result is cached with
     * the name "chartRequestHandler". This can improve performance by not needing to locate, initialize and setup the
     * {@link ChartRequestHandler} each time.
     *
     * @param serviceName Name of the {@link de.schuermann.interactivedata.api.service.annotations.ChartService ChartService}
     * @param chartName Name of the {@link de.schuermann.interactivedata.api.chart.annotations.Chart Chart}
     * @return ChartRequestHandler
     */
    @Cacheable("chartRequestHandler")
    @SuppressWarnings("unchecked")
    public ChartRequestHandler getChartRequestHandler(String serviceName, String chartName) {
        log.debug("Getting ChartRequestHandler for chart with id [" + serviceName + "/" + chartName + "]");
        return createChartRequestHandler(serviceName + "/" + chartName);
    }

    /**
     * Creates and initializes a new {@link ChartRequestHandler} for the given identifier.
     *
     * @param identifier Identifier of the format <code>ServiceName/ChartName</code>
     * @return ChartRequestHandler
     */
    @SuppressWarnings("unchecked")
    private ChartRequestHandler createChartRequestHandler(String identifier) {
        log.debug("Creating new RequestHandler for chart wit id [" + identifier + "]");

        AbstractChartDefinition<?, ?> chartDefinition = chartDefinitionService.getChartDefinition(identifier)
                .orElseThrow(() -> new RequestDataException("No ChartDefinition found for this resource."));

        ChartRequestHandler requestHandler = serviceProvider.getChartRequestHandler(chartDefinition)
                .orElseThrow(() -> new RequestDataException("No ChartRequestHandler found for this resource."));
        requestHandler.initialize(chartDefinition);

        return requestHandler;
    }
}
