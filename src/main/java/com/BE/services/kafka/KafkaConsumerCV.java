package com.BE.services.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.BE.dto.CVDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class KafkaConsumerCV {
    
    @KafkaListener(topics = "secTopic", groupId = "BE")
    public void consume(CVDTO cv) {
        log.info("Consumed message: " + cv);
    }
}
