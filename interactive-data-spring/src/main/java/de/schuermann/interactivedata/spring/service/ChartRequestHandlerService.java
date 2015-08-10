package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.handler.ChartRequestHandler;
import de.schuermann.interactivedata.api.handler.Request;
import de.schuermann.interactivedata.api.service.ChartDefinitionService;
import de.schuermann.interactivedata.api.service.ServiceProvider;
import de.schuermann.interactivedata.spring.web.exceptions.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Philipp Schürmann
 */
@Service
public class ChartRequestHandlerService {

    private Log log = LogFactory.getLog(ChartDefinitionService.class);

    private ServiceProvider serviceProvider;
    private ChartDefinitionService chartDefinitionService;

    @Autowired
    public ChartRequestHandlerService(ServiceProvider serviceProvider, ChartDefinitionService chartDefinitionService) {
        this.serviceProvider = serviceProvider;
        this.chartDefinitionService = chartDefinitionService;
    }

    public ChartData handleDataRequest(Request request) {
        return getChartRequestHandler(request.getName()).handleDataRequest(request);
    }

    @Cacheable("chartRequestHandler")
    public ChartRequestHandler getChartRequestHandler(String name) {
        log.debug("Getting ChartRequestHandler for chart with name: " + name);

        final AbstractChartDefinition<?, ? extends ChartData> chartDefinition = chartDefinitionService.getChartDefinition(name);

        ChartRequestHandler requestHandler = serviceProvider.getChartRequestHandler(chartDefinition)
                .map(handler -> handler)
                .orElseThrow(() -> new ResourceNotFoundException("No ChartRequestHandler found for this resource."));

        requestHandler.setChartDefinition(chartDefinition);

        return requestHandler;
    }

}
