package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.spring.config.InteractiveDataTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * @author Philipp Schürmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = InteractiveDataTestConfiguration.class)
public class ChartDefinitionServiceTest {

    @Autowired
    private ChartDefinitionService chartDefinitionService;

    @Test
    public void testLineChart() throws Exception {
        AbstractChartDefinition chartDefinition = chartDefinitionService.getChartDefinition("line");
        Assert.notNull(chartDefinition);
    }
}
