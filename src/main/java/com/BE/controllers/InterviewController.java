package com.BE.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.InterviewDTO;
import com.BE.services.InterviewService;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;
    
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getInterview(@PathVariable UUID id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InterviewDTO interview = interviewService.getInterview(id, username);
        return ResponseEntity.ok(interview);
    }

    @PatchMapping("/answer")
    public String answerInterview() {
        return "Answer Interview";
    }
}
