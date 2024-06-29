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
                "username", null,
                List.of(new SimpleGrantedAuthority("ROLE_RECRUITER"), new SimpleGrantedAuthority("ROLE_CANDIDATE")));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        when(securityContext.getAuthentication()).thenReturn(authenticationToken);
    }

    @Test
    void testGetAllJobs() {
        // Arrange
        List<JobResultDTO> jobs = new ArrayList<>();
        jobs.add(
                JobResultDTO.builder()
                        .id(UUID.randomUUID())
                        .title("Job 1")
                        .description("Description 1")
                        .status(JobStatus.OPEN)
                        .applicants(1)
                        .offeredInterview(1)
                        .interviewed(1)
                        .minYearsOfExperience(1)
                        .location("Location 1")
                        .salary("Salary 1")
                        .advantages(List.of("Advantage 1"))
                        .additionalInfo("Additional info 1")
                        .mode("Mode 1")
                        .type("Type 1")
                        .experienceLevel("Experience level 1")
                        .responsibilities(List.of("Responsibility 1"))
                        .requirements(List.of("Requirement 1"))
                        .majors(List.of("Major 1"))
                        .skills(List.of("Skill 1"))
                        .closedAt(null)
                        .build());
        jobs.add(
                JobResultDTO.builder()
                        .id(UUID.randomUUID())
                        .title("Job 2")
                        .description("Description 2")
                        .status(null)
                        .applicants(2)
                        .offeredInterview(2)
                        .interviewed(2)
                        .minYearsOfExperience(2)
                        .location("Location 2")
                        .salary("Salary 2")
                        .advantages(List.of("Advantage 2"))
                        .additionalInfo("Additional info 2")
                        .mode("Mode 2")
                        .type("Type 2")
                        .experienceLevel("Experience level 2")
                        .responsibilities(List.of("Responsibility 2"))
                        .requirements(List.of("Requirement 2"))
                        .majors(List.of("Major 2"))
                        .skills(List.of("Skill 2"))
                        .closedAt(null)
                        .build());
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
        jobs.add(
                JobResultDTO.builder()
                        .id(UUID.randomUUID())
                        .title("Job 1")
                        .description("Description 1")
                        .status(JobStatus.OPEN)
                        .applicants(1)
                        .offeredInterview(1)
                        .interviewed(1)
                        .minYearsOfExperience(1)
                        .location("Location 1")
                        .salary("Salary 1")
                        .advantages(List.of("Advantage 1"))
                        .additionalInfo("Additional info 1")
                        .mode("Mode 1")
                        .type("Type 1")
                        .experienceLevel("Experience level 1")
                        .responsibilities(List.of("Responsibility 1"))
                        .requirements(List.of("Requirement 1"))
                        .majors(List.of("Major 1"))
                        .skills(List.of("Skill 1"))
                        .closedAt(null)
                        .build());
        jobs.add(
                JobResultDTO.builder()
                        .id(UUID.randomUUID())
                        .title("Job 2")
                        .description("Description 2")
                        .status(null)
                        .applicants(2)
                        .offeredInterview(2)
                        .interviewed(2)
                        .minYearsOfExperience(2)
                        .location("Location 2")
                        .salary("Salary 2")
                        .advantages(List.of("Advantage 2"))
                        .additionalInfo("Additional info 2")
                        .mode("Mode 2")
                        .type("Type 2")
                        .experienceLevel("Experience level 2")
                        .responsibilities(List.of("Responsibility 2"))
                        .requirements(List.of("Requirement 2"))
                        .majors(List.of("Major 2"))
                        .skills(List.of("Skill 2"))
                        .closedAt(null)
                        .build());
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
        JobResultDTO job = JobResultDTO.builder()
                .id(jobId)
                .title("Job 1")
                .description("Description 1")
                .status(JobStatus.OPEN)
                .applicants(1)
                .offeredInterview(1)
                .interviewed(1)
                .minYearsOfExperience(1)
                .location("Location 1")
                .salary("Salary 1")
                .advantages(List.of("Advantage 1"))
                .additionalInfo("Additional info 1")
                .mode("Mode 1")
                .type("Type 1")
                .experienceLevel("Experience level 1")
                .responsibilities(List.of("Responsibility 1"))
                .requirements(List.of("Requirement 1"))
                .majors(List.of("Major 1"))
                .skills(List.of("Skill 1"))
                .closedAt(null)
                .build();
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
        PostJobRequestDTO postJobRequestDTO = PostJobRequestDTO.builder()
                .title("Job 1")
                .description("Desc 1")
                .yearsOfExperience(1)
                .majors(List.of("Comp Sci"))
                .build();
        when(jobService.createJob(postJobRequestDTO, "username")).thenReturn(PostJobResponseDTO.builder().build());

        // Act
        ResponseEntity<PostJobResponseDTO> response = jobController.postJob(postJobRequestDTO);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(jobService, times(1)).createJob(eq(postJobRequestDTO),
                eq("username"));
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
        verify(jobService, times(1)).apply(eq(jobApplicationRequestDTO), eq("username"));
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
        JobApplicationStatusResponseDTO expectedResponse = new JobApplicationStatusResponseDTO(
                JobApplicationStatus.ACCEPTED);
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
