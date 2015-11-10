package com.github.mrgoro.interactivedata.spring.service;

import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.FilterInfo;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.FunctionInfo;
import com.github.mrgoro.interactivedata.api.chart.definitions.operations.OperationInfo;
import com.github.mrgoro.interactivedata.api.service.ChartDefinitionService;
import com.github.mrgoro.interactivedata.api.util.ReflectionUtil;
import com.github.mrgoro.interactivedata.spring.config.InteractiveDataProperties;
import io.swagger.converter.ModelConverters;
import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.Property;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Service for generating the api documentation of the interactive data
 * framework for use with Swagger.
 *
 * Basic information of the api documentation can be configured using
 * {@link InteractiveDataProperties} e.g. by using Spring Boot
 * property files.
 *
 * @author Philipp Schürmann
 */
@Service
public class SwaggerService {

    private static final Log log = LogFactory.getLog(SwaggerService.class);

    private Swagger swagger = new Swagger();

    /**
     * Get the Swagger Object containing the documentation of the api generated
     * by the interactive data framework.
     *
     * @param host Host of the application
     * @return Swagger documentation
     */
    public Swagger getSwaggerConfiguration(String host) {
        return swagger.host(host);
    }

    @Autowired
    public SwaggerService(ChartDefinitionService chartDefinitionService, InteractiveDataProperties interactiveDataProperties) {
        InteractiveDataProperties.Swagger swaggerConfig = interactiveDataProperties.getSwagger();

        // Basic Info from Properties
        swagger
            .info(new Info()
                .title(swaggerConfig.getTitle())
                .description(swaggerConfig.getDescription())
                .version(swaggerConfig.getVersion())
                .contact(new Contact()
                    .email(swaggerConfig.getContact()))
                .license(new License()
                    .name(swaggerConfig.getLicense())
                    .url(swaggerConfig.getLicenseUrl()))
                .termsOfService(swaggerConfig.getTermsOfServiceUrl())
            )
            .basePath("/"+swaggerConfig.getBasePath())
            .scheme(Scheme.HTTP)
            .produces("application/json");

        if(swaggerConfig.isHttps()) {
            swagger.scheme(Scheme.HTTPS);
        }

        // Add paths from Chart Definitions
        for(AbstractChartDefinition chartDefinition : chartDefinitionService.getChartDefinitions().values()) {
            String name = chartDefinition.getServiceName();
            String serviceName = chartDefinition.getServiceDefinition().getName();
            String serviceDescription = chartDefinition.getServiceDefinition().getDescription();
            if(serviceDescription == null || serviceDescription.isEmpty()) {
                serviceDescription = "APIs for the " + serviceName + " Service";
            }

            swagger.tag(new Tag().name(serviceName).description(serviceDescription));
            swagger.path("/"+name, getPath(chartDefinition));
        }
    }

    /**
     * Return the Swagger {@link Path} representing a specific
     * {@link AbstractChartDefinition ChartDefinition}.
     *
     * @param chartDefinition Chart Definition
     * @return Swagger Path
     */
    private Path getPath(AbstractChartDefinition<?, ?> chartDefinition) {
        Class dataClass = ReflectionUtil.getGenericClass(chartDefinition.getClass(), 1);
        String dataClassString = dataClass.getSimpleName();

        String summary = chartDefinition.getDescription();
        if(summary == null || summary.isEmpty()) {
            summary = dataClassString + " for " + chartDefinition.getName();
        }
        Operation operation = new Operation()
            .tag(chartDefinition.getServiceDefinition().getName())
            .summary(summary)
            .response(200, new Response()
                .description(dataClassString)
                .schema(ModelConverters.getInstance().readAsProperty(dataClass))
            );

        appendModels(dataClass);

        operation.setParameters(getParameters(chartDefinition));

        return new Path().get(operation);
    }

    /**
     * Get all query parameters for a chart definition.
     *
     * Included parameters from Data Request Classes of
     * {@link com.github.mrgoro.interactivedata.api.data.operations.filter.Filter Filters},
     * {@link com.github.mrgoro.interactivedata.api.data.operations.granularity.Granularity Granularites} and
     * {@link com.github.mrgoro.interactivedata.api.data.operations.functions.Function Functions}.
     *
     * @param chartDefinition Chart Definition
     * @return List of Parameters
     */
    private List<Parameter> getParameters(AbstractChartDefinition<?,?> chartDefinition) {
        Map<String, Parameter> parameters = new HashMap<>();

        // Filters
        for(FilterInfo filterInfo : chartDefinition.getFilters()) {
            Class filterDataClass = getRequestDataClass(filterInfo.getFilter());
            parameters.putAll(getParametersForClass(filterDataClass, "Filter"));
        }

        // Operations
        for(OperationInfo operationInfo : chartDefinition.getOperations()) {
            // Granularity
            Class granularityDataClass = getRequestDataClass(operationInfo.getGranularity());
            parameters.putAll(getParametersForClass(granularityDataClass, "Granularity"));

            // Function
            for(FunctionInfo functionInfo: operationInfo.getFunctionInfos()) {
                Class functionDataClass = getRequestDataClass(functionInfo.getFunction());
                parameters.putAll(getParametersForClass(functionDataClass, "Function"));
            }
        }

        return parameters.values()
                .stream()
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(toList());
    }

    /**
     * Get Parameters from a
     * {@link com.github.mrgoro.interactivedata.api.data.operations.OperationData OperationData}-Class.
     *
     * @param dataClass Operation Data Class
     * @param type Type of operation
     * @return Map of parameters with their names as keys
     */
    private Map<String, Parameter> getParametersForClass(Class<?> dataClass, String type) {
        Map<String, Parameter> parameters = new HashMap<>();
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(dataClass).getPropertyDescriptors();
            for(PropertyDescriptor pd : propertyDescriptors) {
                // Class is a property descriptor => filter out
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    parameters.put(
                        pd.getName(),
                        new QueryParameter()
                            .name(pd.getName())
                            .description(type + " param for " + pd.getName())
                            .required(false)
                            .property(getProperty(pd.getPropertyType()))
                    );
                }
            }
        } catch (IntrospectionException e) {
            log.error("Exception using introspection for generating Interactive Data API (Parameters)", e);
        }
        return parameters;
    }

    /**
     * Append a type to the list of availiable models.
     *
     * @param type Type of the model
     */
    private void appendModels(Type type) {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(type);
        for (Map.Entry<String, Model> entry : models.entrySet()) {
            swagger.model(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Get a Swagger Property from a Class.
     *
     * @param clazz Class
     * @return Swagger Property
     */
    private Property getProperty(Class<?> clazz) {
        return ModelConverters.getInstance().readAsProperty(clazz);
    }

    /**
     * Get the Request {@link com.github.mrgoro.interactivedata.api.data.operations.OperationData OperationData}-Class
     * of an {@link com.github.mrgoro.interactivedata.api.data.operations.Operation Operation}.
     *
     * @param operationClass Operation Class
     * @return Request Operation Data
     */
    private Class<?> getRequestDataClass(Class<? extends com.github.mrgoro.interactivedata.api.data.operations.Operation<?,?>> operationClass) {
        return ReflectionUtil.getGenericClass(operationClass, 0);
    }
}
