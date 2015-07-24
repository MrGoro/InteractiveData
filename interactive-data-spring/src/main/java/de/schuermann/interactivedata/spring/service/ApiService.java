package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.spring.rest.AbstractApiBuilder;
import de.schuermann.interactivedata.spring.rest.ApiBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.server.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Philipp Schürmann
 */
@Service
public class ApiService {

    private Log log = LogFactory.getLog(ApiService.class);

    private ApplicationContext applicationContext;
    private ProcessorService processorService;

    @Autowired
    public ApiService(ApplicationContext applicationContext, ProcessorService processorService) {
        this.applicationContext = applicationContext;
        this.processorService = processorService;
    }

    public Resource buildApiResource(@NotNull AbstractChartDefinition chartDefinition) {
        Class<? extends AbstractApiBuilder> apiBuilderClass = processorService.findApiBuilder(chartDefinition.getClass());
        if(apiBuilderClass != null) {
            try {
                Constructor constructor = apiBuilderClass.getConstructor(ApplicationContext.class, chartDefinition.getClass());
                ApiBuilder apiBuilder = (ApiBuilder) constructor.newInstance(applicationContext, chartDefinition);
                return apiBuilder.build();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                log.error("ApiBuilder was unable to build API for ChartDefinition [" + chartDefinition.getClass() + "]: " + e.getMessage());
            }
        } else {
            log.error("Could not find ApiBuilder for ChartDefinition [" + chartDefinition.getClass() + "], skipping chart api");
        }
        return null;
    }
}
