# InteractiveData
Java Library for easy creation of RESTful interfaces for interactive data visualization.

InteractiveData is developed by Philipp Sch&uuml;rmann for his Master Thesis hat University of Hamburg.
The complete document will be provided here after it is finished.

## Project Structure
InteractiveData is divided into multiple modules. See the table below for more information on every module:
```
. InteractiveData
├── interactive-data-core
|     Core framework with basic functionality
├── interactive-data-processors
|     Annotation Processors for JSR 269 Service Locator
├── interactive-data-spring
|     Integration module for Spring Framework
├── interactive-data-spring-boot-starter
|     Starter Template for easy start with Spring Boot
└── interactive-data-spring-sample
      Sample demonstrating the use of this framework with Spring Boot and various frontend libraries
```

## Usage
The following code snippets will show the basic usage of InteractiveData. For additional information see the sample 
project or JavaDoc.

### Maven Dependency
Until the release of the first stable version InteractiveData cannot deploy to Maven Central. 
Snapshots releases are deployed to OSSRH.
Alternatively you can download the source and install the current version to your local maven repository.
Run the following command from the project directory (where this file is located).

```
mvn clean install
```

### Spring Boot
Interactive Data has a starter template for an easy start with Spring Boot. This will also auto configure
everything for direct start.
```
<dependency>
    <groupId>com.github.mrgoro</groupId>
    <artifactId>interactive-data-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Wiki
You can find more information on using and developing with InteractiveData in the wiki. Below you can find some examples:

Develop custom chart types
Develop custom library representations