package com.github.mrgoro.interactivedata.spring.sample.datasource;

import com.github.mrgoro.interactivedata.spring.data.JpaSpecificationDataSource;
import com.github.mrgoro.interactivedata.spring.sample.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Philipp Schürmann
 */
@Repository
public class TemperatureDataSource extends JpaSpecificationDataSource {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Override
    protected JpaSpecificationExecutor getRepository() {
        return temperatureRepository;
    }
}
