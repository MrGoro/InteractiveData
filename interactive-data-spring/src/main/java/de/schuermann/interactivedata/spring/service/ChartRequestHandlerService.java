package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.handler.ChartRequestHandler;
import de.schuermann.interactivedata.api.handler.Request;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Philipp Schürmann
 */
@Service
public class ChartRequestHandlerService {

    public ChartRequestHandlerService() {

    }

    public void handleRequest(Request request) {

    }

    @Cacheable("chartRequestHandler")
    public ChartRequestHandler getChartRequestHandler(String name) {
        return null;
    }
}
