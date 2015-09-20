# InteractiveData
Java Library for easy creation of RESTful interfaces for interactive data visualization.

InteractiveData is developed by Philipp Sch&uuml;rmann for his Master Thesis hat University of Hamburg.
The complete document will be provided here after it is finished.

## Project Structure
InteractiveData is divided into multiple modules. See the table below for more information on every module:
```
. InteractiveData
├── interactive-data-core
|     Core library for basic functionality
├── interactive-data-processors
|     Annotation Processors for JSR 269 Service Locator
├── interactive-data-spring
|     Integration module for Spring Framework
├── interactive-data-spring-boot-starter
|     Starter Template for easy start with Spring Boot
└── interactive-data-spring-sample
      Sample demonstrating the use of this library with Spring Boot and various frontend libraries
```

## Usage
The following code snippets will show the basic usage of InteractiveData. For additional information see the sample 
project or JavaDoc.

### Maven Dependency
InteractiveData currently is not on Maven Central. You have to build and install the project using mvn install.

Dependent on your special case you can choose a existing implementation of the API.
Currently only one implementation for use with Spring is provided. If you choose this implementation add the following
Dependency to your pom.xml.
```
<dependency>
    <groupId>de.schuermann.interactivedata</groupId>
    <artifactId>interactive-data-spring</artifactId>
    <version>1.0</version>
</dependency>
```
You can also make your own implementation for the generic API and make it usable with other frameworks.
See the wiki for more information on developing for other frameworks.

### Spring Boot
Interactive Data has a starter pom.xml for an easy start if you are using Spring Boot. This will also auto configure
everything for direct start.
```
<dependency>
    <groupId>de.schuermann.interactivedata</groupId>
    <artifactId>interactive-data-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

### Wiki
You can find more information on using and developing with InteractiveData in the wiki. Below you can find some examples:

Develop custom chart types
Develop custom library representations