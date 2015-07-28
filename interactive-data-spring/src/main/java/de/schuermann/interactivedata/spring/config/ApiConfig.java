package de.schuermann.interactivedata.spring.config;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.spring.service.ApiService;
import de.schuermann.interactivedata.spring.service.ChartDefinitionService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration for initiating REST APIs with Jersey.
 *
 * @author Philipp Schürmann
 */
@Component
@ApplicationPath("interactivedata")
public class ApiConfig extends ResourceConfig {

    @Autowired
    public ApiConfig(ChartDefinitionService chartDefinitionService, ApiService apiService) {
        List<Resource> resources = new ArrayList<>();
        Collection<AbstractChartDefinition> chartDefinitions = chartDefinitionService.getChartDefinitions().values();
        resources.addAll(chartDefinitions.stream().map(apiService::buildApiResource).collect(Collectors.toList()));
        resources.forEach(this::registerResources);
    }

}