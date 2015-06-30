# InteractiveData
Java Library for easy creation of RESTful interfaces for interactive data visualization.

## Project Structure
InteractiveData is divided into multiple sub modules. The the table below for more information on every project.
```
. InteractiveData
├── interactive-data-api
|     Generic API that can be used by various implementations
├── interactive-data-spring
|     Implementation of the api for use with Spring Boot
└── interactive-data-spring-sample
      Sample demonstrating the use of this library with Spring Boot, D3.js and Highcharts.
```

## Usage
The following code snippets will show the basic usage of InteractiveData. For additional information see the sample 
project or Java Doc.

### Maven Dependency
InteractiveData currently is not on Maven Central. You have to build and install the project using mvn install.
Then add the following to your pom.xml.
```
<dependency>
    <groupId>de.schuermann.interactivedata</groupId>
    <artifactId>interactive-data-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
Dependent on your special case you can choose a existing implementation of the API.
Currently only one implementation for use with Spring is provided. If you choose this implementation add the following
Dependency to your pom.xml.
```
<dependency>
    <groupId>de.schuermann.interactivedata</groupId>
    <artifactId>interactive-data-spring</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
You can also make your own implementation for the generic API and make it usable with other frameworks.

### Declare an interface
```
coming soon...
```