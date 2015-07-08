package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.spring.config.InteractiveDataProperties;
import de.schuermann.interactivedata.spring.data.processors.FilterProcessor;
import de.schuermann.interactivedata.spring.rest.AbstractApiBuilder;
import de.schuermann.interactivedata.spring.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Philipp Schürmann
 */
@Service
public class ProcessorService {

    private String path;

    @Autowired
    public ProcessorService(InteractiveDataProperties properties) {
        this.path = properties.getPath();
    }

    public Class<? extends FilterProcessor> findFilterProcessor(Class<? extends Filter> filterClass) {
        return (Class<? extends FilterProcessor>) ReflectionUtil.getGenericImplementation(FilterProcessor.class, filterClass, path);
    }

    public Class<? extends AbstractApiBuilder> findApiBuilder(Class<? extends AbstractChartDefinition> chartDefinition) {
        return (Class<? extends AbstractApiBuilder>) ReflectionUtil.getGenericExtention(AbstractApiBuilder.class, chartDefinition, path);
    }

}
