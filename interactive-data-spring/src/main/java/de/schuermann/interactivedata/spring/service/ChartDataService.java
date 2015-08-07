package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.ChartDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Philipp Schürmann
 */
@Service
public class ChartDataService {

    @Autowired
    private ChartDefinitionService chartHandlerRegistryService;

    public ChartData getData(String chart, Object request) {
        return null;
    }
}
