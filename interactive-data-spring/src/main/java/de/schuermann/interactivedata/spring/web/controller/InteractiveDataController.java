package de.schuermann.interactivedata.spring.web.controller;

import de.schuermann.interactivedata.api.chart.definitions.ChartDefinitionService;
import de.schuermann.interactivedata.api.handler.Request;
import de.schuermann.interactivedata.spring.service.ChartRequestHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Controller for handling RESTful request.
 *
 * @author Philipp Schürmann
 */
@RestController
public class InteractiveDataController {

    private static final String CHART_NAME = "name";
    private static final String BASE_MAPPING = "/api/{"+CHART_NAME+"}";

    private ChartRequestHandlerService chartRequestHandlerService;
    private ChartDefinitionService chartDefinitionService;

    @Autowired
    public InteractiveDataController(ChartRequestHandlerService chartRequestHandlerService, ChartDefinitionService chartDefinitionService) {
        this.chartRequestHandlerService = chartRequestHandlerService;
        this.chartDefinitionService = chartDefinitionService;
    }

    @RequestMapping(
        value = BASE_MAPPING,
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getData(@PathVariable(CHART_NAME) String chartName, HttpServletRequest servletRequest) {
        servletRequest.getParameterMap();
        return Optional.ofNullable(chartRequestHandlerService.getChartRequestHandler(chartName))
            .map(
                chartRequestHandler -> new ResponseEntity<>(
                    chartRequestHandler.handleDataRequest(buildRequest(chartName, servletRequest)),
                    HttpStatus.OK))
            .orElse(
                    new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
    }

    @RequestMapping(
        value = BASE_MAPPING,
        method = RequestMethod.OPTIONS,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInformation(@PathVariable(CHART_NAME) String chartName) {
        return Optional.ofNullable(chartRequestHandlerService.getChartRequestHandler(chartName))
            .map(
                chartRequestHandler -> new ResponseEntity<>(
                    chartRequestHandler.handleInfoRequest(),
                    HttpStatus.OK))
            .orElse(
                    new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
    }

    private Request buildRequest(String name, HttpServletRequest servletRequest) {
        return new Request();
    }

}