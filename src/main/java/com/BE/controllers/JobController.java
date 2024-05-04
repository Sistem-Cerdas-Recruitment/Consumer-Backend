package com.BE.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.job.JobApplicationRequestDTO;
import com.BE.dto.job.PostJobRequestDTO;
import com.BE.dto.job.PostJobResponseDTO;
import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.services.JobService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    public JobService jobService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllJobs() {
        List<Job> jobs = jobService.findAll();
        Map<String, Object> body = new HashMap<>();
        body.put("data", jobs);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/all/{page}/{size}")
    public String getAllJobs(@PathVariable int page, @PathVariable int size) {
        return "All jobs";
    }

    @GetMapping("{jobId}/application")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<?> getApplication(@PathVariable UUID jobId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobApplication> jobApplications = jobService.findApplications(jobId, user);
        Map<String, Object> body = new HashMap<>();
        body.put("data", jobApplications);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/post")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<?> postJob(@RequestBody @Validated PostJobRequestDTO postJobRequestDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Job job = jobService.createJob(postJobRequestDTO.getTitle(), postJobRequestDTO.getDescription(), user);
        PostJobResponseDTO body = PostJobResponseDTO.builder()
                .message("Job posted successfully")
                .job(job)
                .build();
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/apply")
    @RolesAllowed("CANDIDATE")
    public ResponseEntity<?> applyForJob(@RequestBody JobApplicationRequestDTO body) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplication jobApplication =  jobService.apply(body.getJobId(), body.getCvId(), user);
        return ResponseEntity.ok().body(jobApplication);
    }

}
