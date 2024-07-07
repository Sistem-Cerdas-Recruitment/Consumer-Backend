package com.BE.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class KafkaConsumerTest {

    private Boolean ready = false;
    private String message;

    @KafkaListener(topics = "interview-evaluation", groupId = "BE")
    public void consumeInterview(String message) {
        this.message = message;
        this.ready = true;
    }

    @KafkaListener(topics = "ai-detector", groupId = "BE")
    public void consumeAI(String message) {
        this.message = message;
        this.ready = true;
    }

    public void reset(){
        this.ready = false;
    }
}
