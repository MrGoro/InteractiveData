package com.github.mrgoro.interactivedata.spring.sample.chartapi;

import com.github.mrgoro.interactivedata.spring.sample.datasource.LocationDataSource;
import com.github.mrgoro.interactivedata.api.chart.annotations.Chart;
import com.github.mrgoro.interactivedata.api.chart.annotations.heatmap.Heatmap;
import com.github.mrgoro.interactivedata.api.chart.data.HeatmapData;
import com.github.mrgoro.interactivedata.api.service.annotations.ChartService;
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
