package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.handler.ChartRequestHandler;
import de.schuermann.interactivedata.api.service.ChartDefinitionService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.spring.web.exceptions.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Service
public class ChartRequestHandlerService {

    private Log log = LogFactory.getLog(ChartRequestHandlerService.class);

    private ServiceProvider serviceProvider;
    private ChartDefinitionService chartDefinitionService;

    Map<String, ChartRequestHandler> handlers = new HashMap<>();

    @Autowired
    public ChartRequestHandlerService(ServiceProvider serviceProvider, ChartDefinitionService chartDefinitionService) {
        this.serviceProvider = serviceProvider;
        this.chartDefinitionService = chartDefinitionService;
    }

    @Cacheable("chartRequestHandler")
    @SuppressWarnings("unchecked")
    public ChartRequestHandler getChartRequestHandler(String serviceName, String chartName) {
        String identifier =  serviceName + "/" + chartName;

        log.debug("Getting ChartRequestHandler for chart with name: " + identifier);

        ChartRequestHandler requestHandler = handlers.get(identifier);
        if(requestHandler == null) {
            log.debug("Creating new RequestHandler for chart wit id  [" + identifier + "]");

            AbstractChartDefinition<?, ?> chartDefinition = chartDefinitionService.getChartDefinition(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException("No ChartDefinition found for this resource."));

            requestHandler = serviceProvider.getChartRequestHandler(chartDefinition)
                    .orElseThrow(() -> new ResourceNotFoundException("No ChartRequestHandler found for this resource."));

            requestHandler.setChartDefinition(chartDefinition);

            handlers.put(identifier, requestHandler);
        }
        return requestHandler;
    }

}
