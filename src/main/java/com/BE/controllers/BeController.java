package com.BE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.CVDTO;
import com.BE.entities.User;
import com.BE.services.CVProcessorService;
import com.BE.services.UserService;
import com.BE.services.kafka.KafkaProducer;
import com.BE.services.kafka.KafkaTopicManager;
import com.BE.services.storage.BucketService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api")
public class BeController {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    KafkaTopicManager kafkaTopicManager;

    @Autowired
    UserService userService;

    @Autowired
    CVProcessorService cvProcessorService;

    @Autowired
    BucketService bucketService;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody String name) {

        try {
            userService.createUser(name);
            return "User created successfully!";
        } catch (Exception e) {
            return "Failed to create user: " + e;
        }
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
    public String testKafka(@RequestBody String message) {
        try {
            kafkaProducer.sendMessage(message);
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

    @PostMapping("/processCV")
    public String processCV(@RequestBody CVDTO cv) {

        try {
            cvProcessorService.processCV(cv);
            return "CV processed successfully!";
        } catch (Exception e) {
            return "Failed to process CV: " + e;
        }
    }
}
