package com.BE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.BE.constants.JobStatus;
import com.BE.constants.Role;
import com.BE.controllers.JobController;
import com.BE.dto.job.JobListResponseDTO;
import com.BE.dto.job.JobResultDTO;
import com.BE.dto.job.JobStatusRequestDTO;
import com.BE.dto.job.JobStatusResponseDTO;
import com.BE.dto.job.PostJobRequestDTO;
import com.BE.dto.job.PostJobResponseDTO;
import com.BE.entities.User;
import com.BE.repositories.JobRepository;
import com.BE.security.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JobIntegrationTest {

    // @Autowired
    // private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Mock
    private SecurityContext securityContext;

    @Autowired
    private JobController jobController;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JwtService jwtService;

    private static UUID jobId;

    private final SimpleGrantedAuthority candidateAuthority = new SimpleGrantedAuthority("ROLE_CANDIDATE");

    @BeforeAll
    public static void setUpAll() {
        MockitoAnnotations.openMocks(JobIntegrationTest.class);
    }

    @BeforeEach
    public void setUpEach() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void tearDownEach() {
    }

    @Test
    @SuppressWarnings("null")
    @Order(1)
    public void T_111_testGetAllJobs() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("candidateTest1@mail.com",
                null,
                List.of(candidateAuthority));
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(securityContext.getAuthentication()).thenReturn(auth);
        ResponseEntity<JobListResponseDTO> response = jobController.getAllJobs();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        JobListResponseDTO body = response.getBody();
        assertNotNull(body.getData());
        assertEquals(jobRepository.count(), body.getData().size());
    }

    @Test
    @Order(2)
    public void T_121_testGetJobById() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("candidateTest1@mail.com",
                null,
                List.of(candidateAuthority));
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(securityContext.getAuthentication()).thenReturn(auth);

        ResponseEntity<JobResultDTO> response = jobController
                .getJob(UUID.fromString("ea866954-f5d7-42ce-81ea-1ecf9d849118"));

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(3)
    @SuppressWarnings("null")
    public void T_131_testGetPostedJobs_WithRecruiter() {
        // Mocking the user      
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<JobListResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/posted", HttpMethod.GET, entity,
                JobListResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    @Order(4)
    public void T_132_testGetPostedJobs_WithCandidate() {

        // Mocking the user
        User user = User.builder()
                .email("candidateTest1@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<JobListResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/posted", HttpMethod.GET, entity,
                JobListResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());

    }

    @Test
    @Order(5)
    @Transactional
    @SuppressWarnings("null")
    public void T_141_testPostJob_WithRecruiter() {

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        PostJobRequestDTO request = PostJobRequestDTO.builder()
                .title("Test Job")
                .description("Test Description")
                .location("Test Location")
                .minYearsOfExperience(1)
                .salary("Test Salary")
                .advantages(List.of("Test Advantage"))
                .additionalInfo("Test Additional Info")
                .experienceLevel("Test Experience Level")
                .mode("Test Mode")
                .requirements(List.of("Test Requirement"))
                .responsibilities(List.of("Test Responsibility"))
                .type("Test Type")
                .majors(List.of("Test Major"))
                .build();

        HttpEntity<PostJobRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PostJobResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/post", HttpMethod.POST, entity,
                PostJobResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(jobRepository.findById(response.getBody().getJob().getId()));
        // jobRepository.save(job);
        jobId = response.getBody().getJob().getId();
        jobRepository.deleteById(response.getBody().getJob().getId());
        jobRepository.flush();

    }

    @Test
    @Order(6)
    public void T_142_testPostJob_MissingFields() {

        // Delete the job created in the previous test
        jobRepository.deleteById(jobId);

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        PostJobRequestDTO request = PostJobRequestDTO.builder()
                .description("Test Description")
                .location("Test Location")
                .minYearsOfExperience(1)
                .salary("Test Salary")
                .advantages(List.of("Test Advantage"))
                .additionalInfo("Test Additional Info")
                .experienceLevel("Test Experience Level")
                .mode("Test Mode")
                .requirements(List.of("Test Requirement"))
                .responsibilities(List.of("Test Responsibility"))
                .type("Test Type")
                .majors(List.of("Test Major"))
                .build();

        HttpEntity<PostJobRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PostJobResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/post", HttpMethod.POST, entity,
                PostJobResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());

    }

    @Test
    @Order(7)
    public void T_143_testPostJob_WithCandidate() {

        // Mocking the user
        User user = User.builder()
                .email("candidateTest1@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        PostJobRequestDTO request = PostJobRequestDTO.builder()
                .title("Test Job")
                .description("Test Description")
                .location("Test Location")
                .minYearsOfExperience(1)
                .salary("Test Salary")
                .advantages(List.of("Test Advantage"))
                .additionalInfo("Test Additional Info")
                .experienceLevel("Test Experience Level")
                .mode("Test Mode")
                .requirements(List.of("Test Requirement"))
                .responsibilities(List.of("Test Responsibility"))
                .type("Test Type")
                .majors(List.of("Test Major"))
                .build();

        HttpEntity<PostJobRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PostJobResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/post", HttpMethod.POST, entity,
                PostJobResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(8)
    @SuppressWarnings("null")
    public void T_151_testUpdateJobStatus_WithRecruiter() {

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<JobStatusRequestDTO> entity = new HttpEntity<>(JobStatusRequestDTO.builder()
                .jobId(UUID.fromString("ea866954-f5d7-42ce-81ea-1ecf9d849118"))
                .status(false)
                .build(), headers);

        ResponseEntity<JobStatusResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/status", HttpMethod.PATCH, entity,
                JobStatusResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(JobStatus.CLOSED, response.getBody().getStatus());
    }

    @Test
    @Order(9)
    public void T_152_testUpdateJobStatus_MissingFields() {

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<JobStatusRequestDTO> entity = new HttpEntity<>(JobStatusRequestDTO.builder()
                .jobId(UUID.fromString("ea866954-f5d7-42ce-81ea-1ecf9d849118"))
                .build(), headers);

        ResponseEntity<JobStatusResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/status", HttpMethod.PATCH, entity,
                JobStatusResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @Order(10)
    public void T_153_testUpdateJobStatus_WithOtherRecruiter() {

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest2@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<JobStatusRequestDTO> entity = new HttpEntity<>(JobStatusRequestDTO.builder()
                .jobId(UUID.fromString("ea866954-f5d7-42ce-81ea-1ecf9d849118"))
                .status(false)
                .build(), headers);

        ResponseEntity<JobStatusResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/status", HttpMethod.PATCH, entity,
                JobStatusResponseDTO.class);
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }
}