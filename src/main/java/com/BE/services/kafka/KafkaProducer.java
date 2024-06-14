package com.BE.services.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class KafkaProducer {
    
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    public CompletableFuture<SendResult<Integer, String>> sendMessageAsync(String topic, String message) {
        return kafkaTemplate.send(topic, message);
    }

    public void sendMessage(String topic, Object message) throws Exception{
        String messageStr = objectMapper.writeValueAsString(message);
        CompletableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topic, messageStr);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully: " + result);
            } else {
                log.error("Failed to send message: " + ex);
                throw new RuntimeException(ex);
            }
        });
    }
}
