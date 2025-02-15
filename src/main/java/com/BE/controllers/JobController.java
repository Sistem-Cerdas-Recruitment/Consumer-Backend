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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.job.JobListResponseDTO;
import com.BE.dto.job.JobResultDTO;
import com.BE.dto.job.JobStatusRequestDTO;
import com.BE.dto.job.JobStatusResponseDTO;
import com.BE.dto.job.PostJobRequestDTO;
import com.BE.dto.job.PostJobResponseDTO;
import com.BE.dto.job.application.JobApplicationDTO;
import com.BE.dto.job.application.JobApplicationListResponseDTO;
import com.BE.dto.job.application.JobApplicationRequestDTO;
import com.BE.dto.job.application.JobApplicationResultDTO;
import com.BE.dto.job.application.JobApplicationStatusRequestDTO;
import com.BE.dto.job.application.JobApplicationStatusResponseDTO;
import com.BE.services.job.JobService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/job")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    public JobService jobService;

    @GetMapping("/all")
    public ResponseEntity<JobListResponseDTO> getAllJobs() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobResultDTO> jobs = jobService.findAllOpenJobs(username);
        return ResponseEntity.ok(new JobListResponseDTO(jobs));
    }

    @GetMapping("/all/get")
    public ResponseEntity<Map<String, List<JobResultDTO>>> getAllJobsGet(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String salary,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String recruiter,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobResultDTO> jobs = jobService.findAllOpenJobs(username);
        Map<String, List<JobResultDTO>> body = new HashMap<>();
        body.put("data", jobs);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/posted")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<JobListResponseDTO> getPostedJobs() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobResultDTO> jobs = jobService.findAllByUser(username);
        return ResponseEntity.ok(new JobListResponseDTO(jobs));
    }

    @GetMapping("/all/{page}/{size}")
    public String getAllJobs(@PathVariable int page, @PathVariable int size) {
        return "All jobs";
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResultDTO> getJob(@PathVariable UUID jobId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobResultDTO job = jobService.findJob(jobId, username);
        return ResponseEntity.ok(job);
    }

    @PatchMapping("/status")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<JobStatusResponseDTO> updateJobStatus(@RequestBody @Validated JobStatusRequestDTO request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobStatusResponseDTO job = jobService.updateJobStatus(request.getJobId(), request.getStatus(), username);
        return ResponseEntity.ok(job);
    }

    @PostMapping("/post")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<PostJobResponseDTO> postJob(@RequestBody @Validated PostJobRequestDTO postJobRequestDTO) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostJobResponseDTO body = jobService.createJob(postJobRequestDTO, username);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/applications")
    @RolesAllowed("CANDIDATE")
    public ResponseEntity<JobApplicationListResponseDTO> getApplications() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobApplicationResultDTO> jobApplications = jobService.findApplications(username);
        return ResponseEntity.ok(new JobApplicationListResponseDTO(jobApplications));
    }

    @GetMapping("{jobId}/applications")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<JobApplicationListResponseDTO> getJobApplications(@PathVariable UUID jobId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobApplicationResultDTO> jobApplications = jobService.findApplications(jobId, username);
        return ResponseEntity.ok(new JobApplicationListResponseDTO(jobApplications));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<JobApplicationDTO> getApplication(@PathVariable UUID applicationId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplicationDTO jobApplication;
        
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))) {
            jobApplication = jobService.getRecruiterJobApplication(applicationId, username);
        } else {
            jobApplication = jobService.getCandidateJobApplication(applicationId, username);
        }
        return ResponseEntity.ok(jobApplication);
    }

    @GetMapping("/application/{applicationId}/cv")
    public ResponseEntity<Object> getApplicationCV(@PathVariable UUID applicationId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String cvUrl = jobService.getApplicationCV(applicationId, username);
        Map<String, String> body = new HashMap<>();
        body.put("data", cvUrl);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/application/status")
    @RolesAllowed("RECRUITER")
    public ResponseEntity<JobApplicationStatusResponseDTO> updateApplicationStatus(@RequestBody @Validated JobApplicationStatusRequestDTO request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplicationStatusResponseDTO body = jobService.updateApplicationStatus(request.getJobApplicationId(), request.getIsAccepted(), username);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/apply")
    @RolesAllowed("CANDIDATE")
    public ResponseEntity<JobApplicationDTO> applyForJob(@RequestBody @Validated JobApplicationRequestDTO body) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplicationDTO jobApplicationDto = jobService.apply(body, username);
        return ResponseEntity.ok().body(jobApplicationDto);
    }

}
