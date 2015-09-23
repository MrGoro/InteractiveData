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
Snapshots releases are deployed to OSSRH Snapshot Repository. Add the snapshot repository to your maven project by
adding the following to your pom.xml:
```
<repositories>
    <repository>
        <id>ossrh.sonatype.snapshots.deploy</id>
        <name>OSSRH snapshot repository</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```
Alternatively you can download the source and install the current version to your local maven repository.
Run the following commands:
```
git clone https://github.com/MrGoro/InteractiveData.git
cd InteractiveData
mvn clean install
```

### Spring Boot
Interactive Data has a starter template for an easy start with Spring Boot. This will also auto configure
everything for direct start. Add the following dependency to your Spring Boot Project.
```
<dependency>
    <groupId>com.github.mrgoro</groupId>
    <artifactId>interactive-data-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
To start a new project from scatch use [start.spring.io](https://start.spring.io/) to initzialize a Spring Boot project.
### Wiki
You can find more information on using and developing with InteractiveData in the wiki. Below you can find some examples:

Develop custom chart types
Develop custom library representations