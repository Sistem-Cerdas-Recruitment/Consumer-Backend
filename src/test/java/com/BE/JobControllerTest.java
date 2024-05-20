package com.BE;
import com.BE.controllers.JobController;
import com.BE.dto.job.JobResultDTO;
import com.BE.services.job.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JobControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobController = new JobController();
        jobController.jobService = jobService;
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("username");
    }

    @Test
    void testGetAllJobs() {
        // Arrange
        List<JobResultDTO> jobs = new ArrayList<>();
        jobs.add(new JobResultDTO(UUID.randomUUID(), "Job 1", "Description 1", List.of(), List.of(), null, UUID.randomUUID(), "User 1", false));
        jobs.add(new JobResultDTO(UUID.randomUUID(), "Job 2", "Description 2", List.of(), List.of(), null, UUID.randomUUID(), "User 2", false));
        when(jobService.findAllOpenJobs("username")).thenReturn(jobs);

        // Act
        ResponseEntity<Map<String, List<JobResultDTO>>> response = jobController.getAllJobs();

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Map<String, List<JobResultDTO>> responseBody = response.getBody();
        if(responseBody == null) {
            throw new AssertionError("Response body is null");
        }
        assertEquals(jobs, responseBody.get("data"));
        verify(jobService, times(1)).findAllOpenJobs("username");
    }
}