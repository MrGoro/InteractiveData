package com.github.mrgoro.interactivedata.spring.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.models.*;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;

public class Swagger2JacksonModule extends SimpleModule {

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    context.setMixInAnnotations(Swagger.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Info.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(License.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Scheme.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(SecurityRequirement.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(SecuritySchemeDefinition.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Model.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Property.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Operation.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Path.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Response.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Parameter.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(ExternalDocs.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Xml.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Tag.class, CustomizedSwaggerSerializer.class);
    context.setMixInAnnotations(Contact.class, CustomizedSwaggerSerializer.class);
  }

  @JsonAutoDetect
  @JsonInclude(value = Include.NON_EMPTY)
  private class CustomizedSwaggerSerializer {
  }
}