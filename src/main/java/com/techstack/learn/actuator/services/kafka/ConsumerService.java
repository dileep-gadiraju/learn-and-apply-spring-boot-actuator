package com.techstack.learn.actuator.services.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "test", groupId = "test-consumer")
    public void consumeMessage(String message)  {
        log.info("#######Received from topic=test message={}", message);
    }
}
