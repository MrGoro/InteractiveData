package com.github.mrgoro.interactivedata.spring.web;

import com.github.mrgoro.interactivedata.api.handler.ChartRequest;
import com.github.mrgoro.interactivedata.api.util.exceptions.ChartDefinitionException;
import com.github.mrgoro.interactivedata.api.util.exceptions.RequestDataException;
import com.github.mrgoro.interactivedata.spring.service.ChartRequestHandlerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Controller for exposing visualization data over a RESTful api.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class InteractiveDataRestController {

    private static final Log log = LogFactory.getLog(InteractiveDataRestController.class);

    private static final String SERVICE_NAME = "service";
    private static final String CHART_NAME = "name";
    private static final String BASE_MAPPING = "/api/{"+SERVICE_NAME+"}/{"+CHART_NAME+"}";

    private ChartRequestHandlerService chartRequestHandlerService;

    public InteractiveDataRestController(ChartRequestHandlerService chartRequestHandlerService) {
        this.chartRequestHandlerService = chartRequestHandlerService;
    }

    @RequestMapping(
        value = BASE_MAPPING,
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Cacheable(
        value = "interactivedata/api/data",
        keyGenerator = "RequestParameterKeyGenerator"
    )
    public ResponseEntity<?> getData(@PathVariable(SERVICE_NAME) String serviceName,
                                     @PathVariable(CHART_NAME) String chartName,
                                     HttpServletRequest servletRequest) {

        log.info("Requesting data for chart: service[" + serviceName + "] chart[" + chartName + "]");

        ChartRequest chartRequest = new ChartRequest(chartName, servletRequest.getParameterMap());
        return Optional.ofNullable(chartRequestHandlerService.getChartRequestHandler(serviceName, chartName))
            .map(
                chartRequestHandler -> new ResponseEntity<>(
                        chartRequestHandler.handleDataRequest(chartRequest),
                        HttpStatus.OK)
            ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
    }

    @RequestMapping(
        value = BASE_MAPPING,
        method = RequestMethod.OPTIONS,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInformation(@PathVariable(SERVICE_NAME) String serviceName, @PathVariable(CHART_NAME) String chartName) {

        log.info("Requesting info for chart: service[" + serviceName + "] chart[" + chartName + "]");

        return Optional.ofNullable(chartRequestHandlerService.getChartRequestHandler(serviceName, chartName))
            .map(
                chartRequestHandler -> new ResponseEntity<>(
                        chartRequestHandler.handleInfoRequest(),
                        HttpStatus.OK)
            ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
    }

    @ResponseStatus(
            value= HttpStatus.BAD_REQUEST, // 400
            reason="Your request to a chart was invalid."
    )
    @ExceptionHandler(RequestDataException.class)
    public void badRequest() {}

    @ResponseStatus(
            value= HttpStatus.INTERNAL_SERVER_ERROR, // 500
            reason="The configuration of the chart you requested is invalid. Please contact the administrator."
    )
    @ExceptionHandler(ChartDefinitionException.class)
    public void internalServerError() {}

}