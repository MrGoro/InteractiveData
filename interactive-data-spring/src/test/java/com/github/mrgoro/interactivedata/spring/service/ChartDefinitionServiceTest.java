package com.github.mrgoro.interactivedata.spring.service;

import com.github.mrgoro.interactivedata.api.chart.definitions.AbstractChartDefinition;
import com.github.mrgoro.interactivedata.api.service.ChartDefinitionService;
import com.github.mrgoro.interactivedata.spring.config.InteractiveDataTestConfiguration;
import com.github.mrgoro.interactivedata.spring.service.controllers.ChartController;
import com.github.mrgoro.interactivedata.spring.web.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * @author Philipp Sch&uuml;rmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = InteractiveDataTestConfiguration.class)
public class ChartDefinitionServiceTest {

    @Autowired
    private ChartDefinitionService chartDefinitionService;

    @Test
    public void testLineChart() throws Exception {
        AbstractChartDefinition chartDefinition = chartDefinitionService.getChartDefinition("counter/line")
                .orElseThrow(() -> new ResourceNotFoundException("No ChartRequestHandler found for this resource."));
        Assert.notNull(chartDefinition);
        Assert.isInstanceOf(ChartController.MockDataSource.class.getClass(), chartDefinition.getDataSource());
        Assert.notNull(chartDefinition.getChartPostProcessor());
        Assert.notEmpty(chartDefinition.getFilters());
        Assert.notEmpty(chartDefinition.getOperations());
    }
}
