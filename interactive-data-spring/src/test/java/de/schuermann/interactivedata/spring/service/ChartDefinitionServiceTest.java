package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.service.ChartDefinitionService;
import de.schuermann.interactivedata.spring.config.InteractiveDataTestConfiguration;
import de.schuermann.interactivedata.spring.web.exceptions.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * @author Philipp Sch�rmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = InteractiveDataTestConfiguration.class)
public class ChartDefinitionServiceTest {

    @Autowired
    private ChartDefinitionService chartDefinitionService;

    @Test
    public void testLineChart() throws Exception {
        AbstractChartDefinition chartDefinition = chartDefinitionService.getChartDefinition("line")
                .orElseThrow(() -> new ResourceNotFoundException("No ChartRequestHandler found for this resource."));
        Assert.notNull(chartDefinition);
    }
}
