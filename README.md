# InteractiveData
Java framework for easy creation of (RESTful-)APIs for interactive visualizations.

Interactive Data is developed by Philipp Sch&uuml;rmann for his Master Thesis at University of Hamburg.
The complete document will be provided here after it is finished.

[Sample App on Heroku](https://interactive-data.herokuapp.com/)

[Sample App Source Code](https://github.com/MrGoro/interactive-data-sample)

## Project Structure
Interactive Data is divided into multiple modules. See the table below for more information on modules:
```
. InteractiveData
├── interactive-data-core
|     Core framework with basic functionality
├── interactive-data-processors
|     Annotation Processors for JSR 269 Service Locator
├── interactive-data-spring
|     Integration module for Spring Framework
└── interactive-data-spring-boot-starter
      Starter Template for easy start with Spring Boot
```

## Usage
The following code snippets will show the basic usage of InteractiveData. For additional information see the [Sample 
App](https://github.com/MrGoro/interactive-data-sample) or JavaDoc.

### Maven Dependency
Until the release of the first stable version, InteractiveData cannot deploy to Maven Central. 
Snapshot releases are deployed to OSSRH Snapshot Repository. Add the snapshot repository to your maven project by
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

### Core Framework
The core framework will provide basic functionality. It enables you to define charts and create filters and operations.
Though the core will NOT provide an API on its own. Use one of the integration modules to integrate the framework
into your environment. Add the following dependency to your Maven project:
```
<dependency>
    <groupId>com.github.mrgoro</groupId>
    <artifactId>interactive-data-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Spring Boot
Interactive Data has a starter template for an easy start with Spring Boot. This will auto configure
everything for direct start. Add the following dependency to your Spring Boot project:
```
<dependency>
    <groupId>com.github.mrgoro</groupId>
    <artifactId>interactive-data-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
To start a new project from scratch use [start.spring.io](https://start.spring.io/) to initialize a Spring Boot project.

### Define Charts
Interactive Data uses annotations to define charts whose data is than exposed over an API. The following example shows
a very basic definition of a line chart for visualizing climate data.
```
@ChartService("sensor")
public class SensorCharts {

    @Chart(
        name = "temperature",
        dataSource = TemperatureDataSource.class,
        filter = @FilterDef(
            filter = SearchFilter.class,
            fieldName = "stationId",
            fieldClass = Long.class
        )
    )
    @LineChart(
        axis = {
            @Axis(
                dataField = "time",
                dataType = Date.class,
                type = Axis.Type.X,
                filter = TimeFilter.class,
                granularity = TimeGranularity.class
            ),
            @Axis(
                dataField = "temperature",
                dataType = Double.class,
                type = Axis.Type.Y,
                functions = Average.class
            )
        }
    )
    public LineChartData temperature(LineChartData data) {
        // Further enhance data before sending
        return data;
    }
}
```

## Wiki
You can find more information on using and developing with Interactive Data in the wiki. See some examples below:

* [Integrate Framework](wiki/Integrate-Framework)
* [Define Charts](wiki/Define-Charts)
* [Data Source](wiki/Data-Source)


## License
The InteractiveData framework is released under the [MIT License](http://opensource.org/licenses/MIT).