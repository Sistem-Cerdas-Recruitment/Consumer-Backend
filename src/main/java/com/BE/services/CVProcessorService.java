package com.BE.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BE.dto.CurriculumVitaeDTO;
import com.BE.services.kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CVProcessorService {
    
    @Autowired KafkaProducer kafkaProducer;
    @Autowired ObjectMapper objectMapper;

    public void processCV(CurriculumVitaeDTO cv) throws Exception{
        try {
            String cvJson = objectMapper.writeValueAsString(cv);
            kafkaProducer.sendMessage("first_topic", cvJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message: " + e);
        }
    }
}
