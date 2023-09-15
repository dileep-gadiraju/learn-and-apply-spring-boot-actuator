package com.techstack.learn.actuator.custom.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Karthikeyan Nithiyanandam
 */
@Component("CustomRandChecks")
public class MyCustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        Random random = new Random();

        if(random.nextBoolean()) {
            return Health.down().withDetail("MSG-001", "Random Failure").build();
        }
        return Health.up().build();
    }
}
