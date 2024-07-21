package com.BE.services.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BE.dto.job.matching.MatchingRequestDTO;
import com.BE.services.kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MatchingService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    public void getMatching(MatchingRequestDTO body) {
        try {
            String payload = objectMapper.writeValueAsString(body);
            kafkaProducer.sendMessage("matching", payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }
}
