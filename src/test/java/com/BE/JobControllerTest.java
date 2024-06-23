package com.BE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.BE.constants.JobApplicationStatus;
import com.BE.constants.JobStatus;
import com.BE.controllers.JobController;
import com.BE.dto.job.JobResultDTO;
import com.BE.dto.job.PostJobRequestDTO;
import com.BE.dto.job.PostJobResponseDTO;
import com.BE.dto.job.application.JobApplicationDTO;
import com.BE.dto.job.application.JobApplicationRequestDTO;
import com.BE.dto.job.application.JobApplicationResultDTO;
import com.BE.dto.job.application.JobApplicationStatusRequestDTO;
import com.BE.dto.job.application.JobApplicationStatusResponseDTO;
import com.BE.services.job.JobService;

import lombok.extern.log4j.Log4j2;

@Log4j2
class JobControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private SecurityContext securityContext;

    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobController = new JobController();
        jobController.jobService = jobService;
        SecurityContextHolder.setContext(securityContext);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                "username", null, List.of(new SimpleGrantedAuthority("ROLE_RECRUITER"), new SimpleGrantedAuthority("ROLE_CANDIDATE")));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        when(securityContext.getAuthentication()).thenReturn(authenticationToken);
    }

    @Test
    void testGetAllJobs() {
        // Arrange
        List<JobResultDTO> jobs = new ArrayList<>();
        jobs.add(new JobResultDTO(UUID.randomUUID(), "Job 1", "Description 1", List.of(), List.of(), null,
                2, 1, 0, null, UUID.randomUUID(), "User 1", null, false, null, null, null));
        jobs.add(new JobResultDTO(UUID.randomUUID(), "Job 2", "Description 2", List.of(), List.of(), null,
                1, 1, 0, null, UUID.randomUUID(), "User 2", null, false, null, null, null));
        when(jobService.findAllOpenJobs("username")).thenReturn(jobs);

        // Act
        ResponseEntity<Map<String, List<JobResultDTO>>> response = jobController.getAllJobs();

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Map<String, List<JobResultDTO>> responseBody = response.getBody();
        if (responseBody == null) {
            throw new AssertionError("Response body is null");
        }
        assertEquals(jobs, responseBody.get("data"));
        verify(jobService, times(1)).findAllOpenJobs("username");
    }

    @Test
    void testGetPostedJobs() {
        // Arrange
        List<JobResultDTO> jobs = new ArrayList<>();
        jobs.add(new JobResultDTO(UUID.randomUUID(), "Job 1", "Description 1", List.of(), List.of(), JobStatus.OPEN,
                1, 1, 1, null, UUID.randomUUID(), "User 1", null, false, null, null, null));
        jobs.add(new JobResultDTO(UUID.randomUUID(), "Job 2", "Description 2", List.of(), List.of(), null,
                2, 2, 2, null, UUID.randomUUID(), "User 2", null, false, null, null, null));
        when(jobService.findAllByUser("username")).thenReturn(jobs);

        // Act
        ResponseEntity<Map<String, List<JobResultDTO>>> response = jobController.getPostedJobs();

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Map<String, List<JobResultDTO>> responseBody = response.getBody();
        if (responseBody == null) {
            throw new AssertionError("Response body is null");
        }
        assertEquals(jobs, responseBody.get("data"));
        verify(jobService, times(1)).findAllByUser("username");
    }

    @Test
    void testGetAllJobsByPageAndSize() {
        // Arrange
        int page = 1;
        int size = 10;

        // Act
        String result = jobController.getAllJobs(page, size);

        // Assert
        assertEquals("All jobs", result);
    }

    @Test
    void testGetJob() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        JobResultDTO job = new JobResultDTO(jobId, "Job 1", "Description 1", List.of(), List.of(), null,
                1, 1, 1, null, UUID.randomUUID(), "User 1", null, false, null, null, null);
        when(jobService.findJob(jobId, "username")).thenReturn(job);

        // Act
        ResponseEntity<JobResultDTO> response = jobController.getJob(jobId);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        JobResultDTO responseBody = response.getBody();
        if (responseBody == null) {
            throw new AssertionError("Response body is null");
        }
        assertEquals(job, responseBody);
        verify(jobService, times(1)).findJob(jobId, "username");
    }

    @Test
    void testGetJobApplications() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        List<JobApplicationResultDTO> jobApplications = new ArrayList<>();
        jobApplications.add(JobApplicationResultDTO.builder()
                .id(UUID.randomUUID())
                .jobId(jobId)
                .jobTitle("Job 1")
                .recruiterId(UUID.randomUUID())
                .recruiterName("Recruiter 1")
                .userId(UUID.randomUUID())
                .userName("User 1")
                .build());
        jobApplications.add(JobApplicationResultDTO.builder()
                .id(UUID.randomUUID())
                .jobId(jobId)
                .jobTitle("Job 2")
                .recruiterId(UUID.randomUUID())
                .recruiterName("Recruiter 2")
                .userId(UUID.randomUUID())
                .userName("User 2")
                .build());
        when(jobService.findApplications(jobId, "username")).thenReturn(jobApplications);

        // Act
        ResponseEntity<Map<String, List<JobApplicationResultDTO>>> response = jobController.getJobApplications(jobId);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Map<String, List<JobApplicationResultDTO>> responseBody = response.getBody();
        if (responseBody == null) {
            throw new AssertionError("Response body is null");
        }
        assertEquals(jobApplications, responseBody.get("data"));
        verify(jobService, times(1)).findApplications(jobId, "username");
    }

    @Test
    void testPostJob() {
        // Arrange
        PostJobRequestDTO postJobRequestDTO = new PostJobRequestDTO("Job 1", "Desc 1", 1, List.of("Comp Sci"), List.of("Java"));

        // Act
        ResponseEntity<PostJobResponseDTO> response = jobController.postJob(postJobRequestDTO);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(jobService, times(1)).createJob(eq("Job 1"), eq("Desc 1"), eq(List.of("Comp Sci")), eq(List.of("Java")), eq("username"));
    }

    @Test
    void testApplyForJob() {
        // Arrange
        JobApplicationRequestDTO jobApplicationRequestDTO = new JobApplicationRequestDTO(UUID.randomUUID(),
                UUID.randomUUID(), null);

        // Act
        ResponseEntity<JobApplicationDTO> response = jobController.applyForJob(jobApplicationRequestDTO);
        log.info("Response: {}", response);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(jobService, times(1)).apply(eq(jobApplicationRequestDTO.getJobId()),
                eq(jobApplicationRequestDTO.getCvId()), eq(null), eq("username"));
    }

    @Test
    void testGetApplication() {
        // Arrange
        UUID applicationId = UUID.randomUUID();
        JobApplicationDTO jobApplication = JobApplicationDTO.builder()
                .id(applicationId)
                .status(JobApplicationStatus.PENDING)
                .jobId(UUID.randomUUID())
                .jobTitle("Job 1")
                .recruiterId(UUID.randomUUID())
                .recruiterName("Recruiter 1")
                .userId(UUID.randomUUID())
                .userName("User 1")
                .build();
        when(jobService.getRecruiterJobApplication(applicationId, "username")).thenReturn(jobApplication);

        // Act
        ResponseEntity<JobApplicationDTO> response = jobController.getApplication(applicationId);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        JobApplicationDTO responseBody = response.getBody();

        assertEquals(jobApplication, responseBody);
        verify(jobService, times(1)).getRecruiterJobApplication(applicationId, "username");
    }

    @Test
    void testUpdateApplicationStatus() {
        // Arrange
        UUID jobApplicationId = UUID.randomUUID();
        boolean isAccepted = true;
        JobApplicationStatusRequestDTO request = new JobApplicationStatusRequestDTO(jobApplicationId, isAccepted);
        JobApplicationStatusResponseDTO expectedResponse = new JobApplicationStatusResponseDTO(JobApplicationStatus.ACCEPTED);
        when(jobService.updateApplicationStatus(jobApplicationId, isAccepted, "username")).thenReturn(expectedResponse);

        // Act
        ResponseEntity<JobApplicationStatusResponseDTO> response = jobController.updateApplicationStatus(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JobApplicationStatusResponseDTO responseBody = response.getBody();
        assertEquals(expectedResponse, responseBody);
        verify(jobService, times(1)).updateApplicationStatus(jobApplicationId, isAccepted, "username");
    }
}


    
