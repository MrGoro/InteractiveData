package com.github.mrgoro.interactivedata.spring.service.locators;

import com.github.mrgoro.interactivedata.api.data.source.DataSource;
import com.github.mrgoro.interactivedata.api.service.ServiceLocator;
import com.github.mrgoro.interactivedata.api.service.ServiceProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Service for searching the correct implementations of interfaces.
 *
 * @author Philipp Sch&uuml;rmann
 */
@Service
public class SpringServiceProvider extends ServiceProvider {

    private ApplicationContext applicationContext;

    @Autowired
    public SpringServiceProvider(ApplicationContext applicationContext, ServiceLocator serviceLocator) {
        super(serviceLocator);
        this.applicationContext = applicationContext;
    }

    @Override
    protected <T> T getInstanceOfClass(Class<T> tClass) {
        // Try to instantiate with Spring, else use default
        try {
            return applicationContext.getBean(tClass);
        } catch (BeansException | IllegalArgumentException e) {
            return super.getInstanceOfClass(tClass);
        }
    }

    @Override
    public DataSource getDataSource(Class<? extends DataSource> dataSourceClass) {
        return getInstanceOfClass(dataSourceClass);
    }
}
