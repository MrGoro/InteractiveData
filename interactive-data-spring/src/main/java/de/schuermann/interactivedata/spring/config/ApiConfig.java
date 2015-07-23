package de.schuermann.interactivedata.spring.config;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.spring.service.ApiService;
import de.schuermann.interactivedata.spring.service.ReflectionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import java.util.ArrayList;
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
    public ApiConfig(ReflectionService reflectionService, ApiService apiService) {
        List<Resource> resources = new ArrayList<>();
        List<AbstractChartDefinition> chartDefinitions = reflectionService.getChartDefinitions();
        resources.addAll(chartDefinitions.stream().map(apiService::buildApiResource).collect(Collectors.toList()));
        resources.forEach(this::registerResources);
    }

}
