package de.schuermann.interactivedata.spring.web;

import de.schuermann.interactivedata.api.handler.ChartRequest;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;
import de.schuermann.interactivedata.api.util.exceptions.RequestDataException;
import de.schuermann.interactivedata.spring.service.ChartRequestHandlerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Controller for handling RESTful request and forwarding a request to a ChartRequestHandler.
 *
 * @author Philipp Sch&uuml;rmann
 */
@RestController
public class InteractiveDataController {

    private static final Log log = LogFactory.getLog(InteractiveDataController.class);

    private static final String SERVICE_NAME = "service";
    private static final String CHART_NAME = "name";
    private static final String BASE_MAPPING = "/api/{"+SERVICE_NAME+"}/{"+CHART_NAME+"}";

    private ChartRequestHandlerService chartRequestHandlerService;

    @Autowired
    public InteractiveDataController(ChartRequestHandlerService chartRequestHandlerService) {
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
    public ResponseEntity<?> getInformation(@PathVariable(SERVICE_NAME) String serviceName, @PathVariable(CHART_NAME) String chartName) {

        log.info("Requesting info for chart: service[" + serviceName + "] chart[" + chartName + "]");

        return Optional.ofNullable(chartRequestHandlerService.getChartRequestHandler(serviceName, chartName))
            .map(
                chartRequestHandler -> new ResponseEntity<>(
                        chartRequestHandler.handleInfoRequest(),
                        HttpStatus.OK))
            .orElse(
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