package com.msa.demo.sockshop.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.msa.demo.sockshop.entities.HealthCheck;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ITHealthCheckController {

    @Autowired
    private HealthCheckController healthCheckController;

    @Test
    public void getHealthCheck() throws Exception {
        Map<String, List<HealthCheck>> healthChecks = healthCheckController.getHealth();
        assertThat(healthChecks.get("health").size(), is(equalTo(2)));
    }

}
