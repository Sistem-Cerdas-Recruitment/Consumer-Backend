package com.BE.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.interview.InterviewAnswerRequestDTO;
import com.BE.dto.interview.InterviewDTO;
import com.BE.dto.interview.InterviewResponseDTO;
import com.BE.dto.interview.InterviewScoreRequestDTO;
import com.BE.dto.interview.InterviewStartRequestDTO;
import com.BE.services.job.InterviewService;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @GetMapping("/get/{id}")
    public ResponseEntity<InterviewDTO> getInterview(@PathVariable UUID id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InterviewDTO interview = interviewService.getInterview(id, username);
        return ResponseEntity.ok(interview);
    }

    @PatchMapping("/answer")
    public ResponseEntity<InterviewResponseDTO> answerInterview(@RequestBody InterviewAnswerRequestDTO request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InterviewResponseDTO body = interviewService.answer(request.getJobApplicationId(), username, request.getChat());
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/score")
    public ResponseEntity<String> scoreInterview(@RequestBody InterviewScoreRequestDTO request) {
        interviewService.score(request.getJobApplicationId(), request.getInterviewScore());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/start")
    public ResponseEntity<InterviewResponseDTO> startInterview(@RequestBody InterviewStartRequestDTO request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InterviewResponseDTO body = interviewService.start(request.getJobApplicationId(), username);
        return ResponseEntity.ok(body);
    }
}
