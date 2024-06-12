package com.BE.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.entities.User;
import com.BE.services.antiCheat.AntiCheatService;
import com.BE.services.kafka.KafkaProducer;
import com.BE.services.kafka.KafkaTopicManager;
import com.BE.services.storage.BucketService;
import com.BE.services.user.UserService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api")
@RolesAllowed("ADMIN")
public class BeController {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    KafkaTopicManager kafkaTopicManager;

    @Autowired
    UserService userService;

    @Autowired
    BucketService bucketService;

    @Autowired
    AntiCheatService antiCheatService;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/getUser/{name}")
    @RolesAllowed("ADMIN")
    public User getUser(@PathVariable String name) {
        
        try {
            return userService.getUser(name);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/testKafka")
    public String testKafka(@RequestBody Map<String, String> body) {
        try {
            kafkaProducer.sendMessage(body.get("topic"), body.get("message"));
            return "Message sent successfully!";
        } catch (Exception e) {
            return "Failed to send message: " + e;
        }
    }

    @PostMapping("/registerTopic")
    public String registerTopic(@RequestBody String topicName) {
        kafkaTopicManager.createTopic(topicName);
        return "Topic registered successfully!";
    }

    @PostMapping("/clearTopic")
    public String clearTopic(@RequestBody String topicName) {
        kafkaTopicManager.clearMsg(topicName);
        return "Topic cleared successfully!";
    }
}
