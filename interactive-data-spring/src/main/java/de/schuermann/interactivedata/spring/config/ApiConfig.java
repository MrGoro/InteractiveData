package de.schuermann.interactivedata.spring.config;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.spring.service.ApiService;
import de.schuermann.interactivedata.spring.service.ReflectionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for initiating REST APIs with Jersey.
 *
 * @author Philipp Schürmann
 */
@Component
@ApplicationPath("interactivedata")
public class ApiConfig extends ResourceConfig {

    private Log log = LogFactory.getLog(ResourceConfig.class);

    @Autowired
    public ApiConfig(ServletContext servletContext, ReflectionService reflectionService, ApiService apiService) {
        List<Resource> resources = new ArrayList<>();

        List<AbstractChartDefinition> chartDefinitions = reflectionService.getChartDefinitions();
        for(AbstractChartDefinition chartDefinition : chartDefinitions) {
            resources.add(apiService.buildApiResource(chartDefinition));
        }
        WebApplicationContext springFactory = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        resources.forEach(this::registerResources);
    }

}
