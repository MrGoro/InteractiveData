package com.github.mrgoro.interactivedata.spring.web;

import com.github.mrgoro.interactivedata.spring.service.SwaggerService;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;

/**
 * Controller for exposing Swagger documentation as JSON.
 *
 * @author Philipp Schürmann
 */
@RestController
public class SwaggerController {

    @Autowired
    private SwaggerService swaggerService;

    @CrossOrigin
    @RequestMapping(
        method = RequestMethod.GET,
        path = "/api/swagger.json"
    )
    public Swagger getSwaggerJson(ServletContext servletContext) {
        return swaggerService.getSwaggerConfiguration(servletContext.getContextPath());
    }
}
