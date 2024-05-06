package com.BE.services.kafka;

import java.util.List;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

@Service
public class KafkaTopicManager {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    private AdminClient adminClient;

    public void createTopic(String topicName) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(topicName, 3, (short) 1));
    }

    public void clearMsg(String topicName) {
        adminClient.deleteTopics(List.of(topicName));
    }
    
}
