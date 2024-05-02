package com.BE.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BE.dto.InterviewChatLogDTO;
import com.BE.services.kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AntiCheatService {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    public void checkForCheating(List<InterviewChatLogDTO> body) {
        try {
            String payload = objectMapper.writeValueAsString(body);
            kafkaProducer.sendMessage("ai-detector", payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }
}
