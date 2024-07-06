package com.BE;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.BE.constants.JobApplicationStatus;
import com.BE.constants.Role;
import com.BE.dto.job.application.JobApplicationDTO;
import com.BE.dto.job.application.JobApplicationListResponseDTO;
import com.BE.dto.job.application.JobApplicationRequestDTO;
import com.BE.dto.job.application.JobApplicationStatusRequestDTO;
import com.BE.dto.job.application.JobApplicationStatusResponseDTO;
import com.BE.entities.User;
import com.BE.repositories.JobApplicationRepository;
import com.BE.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JobApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    private static UUID jobApplicationId;

    @Test
    @Order(1)
    @SuppressWarnings("null")
    public void T_211_testGetJobApplications() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<JobApplicationListResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/{id}/applications", HttpMethod.GET, entity,
                JobApplicationListResponseDTO.class,
                "6cd5cac6-6cb5-4222-a209-6f4ae591a593");

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(jobApplicationRepository.count(), response.getBody().getData().size());
    }

    @Test
    @Order(2)
    public void T_212_testGetJobApplications_WithOtherUser() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest2@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<JobApplicationListResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/{id}/applications", HttpMethod.GET, entity,
                JobApplicationListResponseDTO.class,
                "6cd5cac6-6cb5-4222-a209-6f4ae591a593");

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(3)
    @SuppressWarnings("null")
    public void T_221_testCandidateJobApplications() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest1@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<JobApplicationListResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/applications", HttpMethod.GET, entity,
                JobApplicationListResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    @Order(4)
    public void T_222_testCandidateJobApplications_WithRecruiter() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest2@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<JobApplicationListResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/applications", HttpMethod.GET, entity,
                JobApplicationListResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(5)
    @SuppressWarnings("null")
    public void T_231_testGetJobApplication() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<JobApplicationDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/application/{id}", HttpMethod.GET, entity,
                JobApplicationDTO.class,
                "1cc0f4b9-206e-4809-b6f2-1252af69c9d4");

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("1cc0f4b9-206e-4809-b6f2-1252af69c9d4", response.getBody().getId().toString());
    }

    @Test
    @Order(6)
    public void T_232_testGetJobApplication_WithOtherUser() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest2@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<JobApplicationDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/application/{id}", HttpMethod.GET, entity,
                JobApplicationDTO.class,
                "1cc0f4b9-206e-4809-b6f2-1252af69c9d4");

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(7)
    @SuppressWarnings({ "unchecked", "null" })
    public void T_241_testApplyJob() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest1@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String experienceStr = null;
        experienceStr = "{ \"educations\": [], \"experiences\": [ { \"position\": \"Frontend Developer Intern\", \"company\": \"Andalin\", \"start\": \"2023-05-20\", \"end\": \"2023-08-20\", \"description\": \"\" } ], \"skills\": [ \"ReactJS\", \"TailwindCSS\", \"PHP\", \"Redis\", \"NodeJS (\", \"Express )\", \"MySQL\", \"PostgreSQL\", \"Java (\", \"SOAP )\", \"Docker\", \"Typescript\", \"Python\", \"C\", \"C + +\", \"Ruby\", \"Go\", \"Java\", \"MySQL\", \"PostgreSQL\", \"MongoDB\", \"Redis\", \"React\", \"Vue\", \"Express\", \"Ruby on Rails\", \"Go Gin\", \"TailwindCSS\", \"ChakraUI\", \"Docker\", \"Git\", \"Jira\", \"Trello\", \"Google Cloud Platform ( GCP )\", \"Netlify\", \"Vercel\" ] }";
        Map<String, Object> experience = null;
        try {
            experience = objectMapper.readValue(experienceStr, Map.class);
        } catch (Exception e) {
        }

        JobApplicationRequestDTO body = new JobApplicationRequestDTO(
                UUID.fromString("ea866954-f5d7-42ce-81ea-1ecf9d849118"),
                UUID.fromString("2f1b1f18-e26c-442f-938e-90730b0d125d"),
                experience);

        HttpEntity<JobApplicationRequestDTO> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<JobApplicationDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/apply", HttpMethod.POST, entity,
                JobApplicationDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        jobApplicationId = response.getBody().getId();
    }

    @Test
    @Order(8)
    public void T_242_testApplyJob_MissingField() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest1@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String experience = "{ \"educations\": [], \"experiences\": [ { \"position\": \"Frontend Developer Intern\", \"company\": \"Andalin\", \"start\": \"2023-05-20\", \"end\": \"2023-08-20\", \"description\": \"\" } ], \"skills\": [ \"ReactJS\", \"TailwindCSS\", \"PHP\", \"Redis\", \"NodeJS (\", \"Express )\", \"MySQL\", \"PostgreSQL\", \"Java (\", \"SOAP )\", \"Docker\", \"Typescript\", \"Python\", \"C\", \"C + +\", \"Ruby\", \"Go\", \"Java\", \"MySQL\", \"PostgreSQL\", \"MongoDB\", \"Redis\", \"React\", \"Vue\", \"Express\", \"Ruby on Rails\", \"Go Gin\", \"TailwindCSS\", \"ChakraUI\", \"Docker\", \"Git\", \"Jira\", \"Trello\", \"Google Cloud Platform ( GCP )\", \"Netlify\", \"Vercel\" ] }";
        JobApplicationRequestDTO body = new JobApplicationRequestDTO(
                UUID.fromString("ea866954-f5d7-42ce-81ea-1ecf9d849118"),
                null,
                experience);

        HttpEntity<JobApplicationRequestDTO> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/apply", HttpMethod.POST, entity,
                Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @Order(9)
    public void T_243_testApplyJob_WithRecruiter() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        String experience = "";

        JobApplicationRequestDTO body = new JobApplicationRequestDTO(
                UUID.fromString("ea866954-f5d7-42ce-81ea-1ecf9d849118"),
                UUID.fromString("2f1b1f18-e26c-442f-938e-90730b0d125d"),
                experience);

        HttpEntity<JobApplicationRequestDTO> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/apply", HttpMethod.POST, entity,
                Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(10)
    @SuppressWarnings("null")
    public void T_251_testUpdateJobApplicationStatus() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<JobApplicationStatusRequestDTO> entity = new HttpEntity<>(
                new JobApplicationStatusRequestDTO(jobApplicationId, true),
                headers);

        // When
        ResponseEntity<JobApplicationStatusResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/application/status", HttpMethod.PATCH, entity,
                JobApplicationStatusResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(JobApplicationStatus.AWAITING_INTERVIEW, response.getBody().getStatus());
    }

    @Test
    @Order(11)
    public void T_252_testUpdateJobApplicationStatus_MissingField() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<JobApplicationStatusRequestDTO> entity = new HttpEntity<>(
                new JobApplicationStatusRequestDTO(jobApplicationId, null),
                headers);

        // When
        ResponseEntity<JobApplicationStatusResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/application/status", HttpMethod.PATCH, entity,
                JobApplicationStatusResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @Order(12)
    public void T_253_testUpdateJobApplicationStatus_WithOtherUser() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest2@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<JobApplicationStatusRequestDTO> entity = new HttpEntity<>(
                new JobApplicationStatusRequestDTO(jobApplicationId, true),
                headers);

        // When
        ResponseEntity<JobApplicationStatusResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/job/application/status", HttpMethod.PATCH, entity,
                JobApplicationStatusResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }
}
