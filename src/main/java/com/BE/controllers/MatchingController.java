package com.BE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.job.matching.RelevanceUpdateRequestDTO;
import com.BE.services.job.JobService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = "*")
public class MatchingController {

    @Autowired
    private JobService jobService;
    
    @PatchMapping("")
    @RolesAllowed("ADMIN")
    public ResponseEntity<String> updateRelevanceScore(@RequestBody @Validated RelevanceUpdateRequestDTO request){
        jobService.updateRelevanceScore(request);
        return ResponseEntity.ok("Success");
    }

}
