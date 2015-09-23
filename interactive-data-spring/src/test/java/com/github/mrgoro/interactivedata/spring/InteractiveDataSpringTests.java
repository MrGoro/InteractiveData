package com.github.mrgoro.interactivedata.spring;

import com.github.mrgoro.interactivedata.spring.config.InteractiveDataTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = InteractiveDataTestConfiguration.class)
public class InteractiveDataSpringTests {

    @Test
    public void contextLoads() {
    }

}