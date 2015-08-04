package de.schuermann.interactivedata.spring.web.controller;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.spring.service.ChartDefinitionService;
import de.schuermann.interactivedata.spring.web.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Philipp Schürmann
 */
@Controller
public class InteractiveDataController {

    private static final String BASE_MAPPING = "/api/{name}";

    @Autowired
    private ChartDefinitionService chartDefinitionService;

    public InteractiveDataController() {

    }

    @ResponseBody
    @RequestMapping(value = BASE_MAPPING, method = RequestMethod.GET)
    public ResponseEntity<?> getData(@PathVariable("name") String chartName, HttpServletRequest servletRequest) {
        AbstractChartDefinition chartDefinition = chartDefinitionService.getChartDefinition(chartName);
        if(chartDefinition == null) {
            throw new ResourceNotFoundException("No chart with name [" + chartName + "] found.");
        }

        servletRequest.getParameterMap();
        return null;
    }
}
