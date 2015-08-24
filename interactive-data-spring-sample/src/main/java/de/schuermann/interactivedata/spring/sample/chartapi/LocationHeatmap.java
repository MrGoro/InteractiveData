package de.schuermann.interactivedata.spring.sample.chartapi;

import de.schuermann.interactivedata.api.chart.annotations.Chart;
import de.schuermann.interactivedata.api.chart.annotations.heatmap.Heatmap;
import de.schuermann.interactivedata.api.chart.data.HeatmapData;
import de.schuermann.interactivedata.api.service.annotations.ChartService;
import de.schuermann.interactivedata.spring.sample.datasource.LocationDataSource;
import org.springframework.stereotype.Service;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Service
@ChartService("location")
public class LocationHeatmap {

    @Chart(
        name = "heatmap",
        dataSource = LocationDataSource.class
    )
    @Heatmap(

    )
    public HeatmapData getLocationHeatmap(HeatmapData data) {
        return data;
    }
}
