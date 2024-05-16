package com.BE.services.antiCheat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BE.constants.JobApplicationStatus;
import com.BE.dto.antiCheat.AntiCheatEvaluationDTO;
import com.BE.dto.antiCheat.AntiCheatResultDTO;
import com.BE.services.job.JobService;
import com.BE.services.kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AntiCheatService {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private JobService jobService;

    public void checkForCheating(AntiCheatEvaluationDTO body) {
        try {
            String payload = objectMapper.writeValueAsString(body);
            kafkaProducer.sendMessage("ai-detector", payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }

    public void updateResult(AntiCheatResultDTO body) {
        jobService.updateJobApplication(body.getJobApplicationId(), JobApplicationStatus.EVALUATED, body.getEvaluations());
    }
}
