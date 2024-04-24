package com.BE.services.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class KafkaConsumer {
    
    @KafkaListener(topics = "first_topic", groupId = "BE")
    public void consume(String message) {
        log.info("Consumed message: " + message);
    }
}
