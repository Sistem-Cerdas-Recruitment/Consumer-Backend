package com.BE.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.job.JobApplicationDTO;
import com.BE.dto.job.JobApplicationRequestDTO;
import com.BE.dto.job.JobApplicationResultDTO;
import com.BE.dto.job.JobResultDTO;
import com.BE.dto.job.PostJobRequestDTO;
import com.BE.dto.job.PostJobResponseDTO;
import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.services.JobService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    public JobService jobService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllJobs() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobResultDTO> jobs = jobService.findAllOpenJobs(username);
        Map<String, Object> body = new HashMap<>();
        body.put("data", jobs);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/posted")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<?> getPostedJobs() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobResultDTO> jobs = jobService.findAllByUser(username);
        Map<String, Object> body = new HashMap<>();
        body.put("data", jobs);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/all/{page}/{size}")
    public String getAllJobs(@PathVariable int page, @PathVariable int size) {
        return "All jobs";
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJob(@PathVariable UUID jobId) {
        JobResultDTO job = jobService.findJob(jobId);
        return ResponseEntity.ok(job);
    }

    @GetMapping("{jobId}/applications")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<?> getJobApplications(@PathVariable UUID jobId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobApplicationResultDTO> jobApplications = jobService.findApplications(jobId, username);
        Map<String, Object> body = new HashMap<>();
        body.put("data", jobApplications);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<?> getApplication(@PathVariable UUID applicationId) {
        // TODO: Check
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplicationDTO jobApplication;
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))) {
            jobApplication = jobService.getRecruiterJobApplication(applicationId, username);
        } else{
            jobApplication = jobService.getCandidateJobApplication(applicationId, username);
        }
        return ResponseEntity.ok(jobApplication);
    }

    @PostMapping("/post")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<?> postJob(@RequestBody @Validated PostJobRequestDTO postJobRequestDTO) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Job job = jobService.createJob(postJobRequestDTO.getTitle(), postJobRequestDTO.getDescription(), username);
        PostJobResponseDTO body = PostJobResponseDTO.builder()
                .message("Job posted successfully")
                .job(job)
                .build();
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/apply")
    @RolesAllowed("CANDIDATE")
    public ResponseEntity<?> applyForJob(@RequestBody JobApplicationRequestDTO body) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplication jobApplication =  jobService.apply(body.getJobId(), body.getCvId(), username);
        return ResponseEntity.ok().body(jobApplication);
    }

}
