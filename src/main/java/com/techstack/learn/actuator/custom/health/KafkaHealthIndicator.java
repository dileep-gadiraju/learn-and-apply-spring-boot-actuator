package com.techstack.learn.actuator.custom.health;

import com.techstack.learn.actuator.services.LogOutputGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component("Kafka")
public class KafkaHealthIndicator  implements HealthIndicator {

    private static Logger log = LoggerFactory.getLogger(KafkaHealthIndicator.class);

    @Autowired
    private KafkaTemplate<String, Object> KafkaTemplate;

    @Value("${kafka.health.indicator.timeout.ms:100}")
    private int timeout;

    public KafkaHealthIndicator(KafkaTemplate<String, Object> KafkaTemplate) {
        this.KafkaTemplate = KafkaTemplate;
    }

    @PostConstruct
    void postConstruction() {
        log.info("[KafkaHealthIndicator] timeout: {}", timeout);
    }

    /**
     * Return an indication of health.
     *
     * @return the health for
     */
    @Override
    public Health health() {
        try {
            java.util.Map<org.apache.kafka.common.MetricName, ? extends org.apache.kafka.common.Metric> map=KafkaTemplate.metrics();
        } catch (Exception e) {
            log.error("[kafka-health-indicator]: Kafka Health Down! Caught cause: {}", e);
            return Health.down(e).build();
        }
        return Health.up().build();
    }
}
