package com.BE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.BE.dto.interview.InterviewAnswerRequestDTO;
import com.BE.dto.interview.InterviewChatDTO;
import com.BE.dto.interview.InterviewDTO;
import com.BE.dto.interview.InterviewResponseDTO;
import com.BE.dto.interview.InterviewScoreRequestDTO;
import com.BE.dto.interview.InterviewStartRequestDTO;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.repositories.JobApplicationRepository;
import com.BE.security.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InterviewIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${service.x-api-key}")
    private String apiKey;

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    private static String question;

    @Test
    @Order(1)
    public void T_411_testGetInterviewHistory() {
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
        ResponseEntity<InterviewDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/get/{id}",
                HttpMethod.GET, entity, InterviewDTO.class,
                UUID.fromString("1cc0f4b9-206e-4809-b6f2-1252af69c9d4"));

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    }

    @Test
    @Order(2)
    public void T_412_testGetJobApplication_AfterInterview() {
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

        JobApplication application = jobApplicationRepository
                .findById(UUID.fromString("1cc0f4b9-206e-4809-b6f2-1252af69c9d4")).get();
        application.setStatus(JobApplicationStatus.EVALUATED);
        jobApplicationRepository.save(application);

        // When
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:" + port + "/api/interview/get/{id}",
                HttpMethod.GET, entity, Object.class,
                UUID.fromString("1cc0f4b9-206e-4809-b6f2-1252af69c9d4"));

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
        application.setStatus(JobApplicationStatus.INTERVIEW);
        jobApplicationRepository.save(application);
    }

    @Test
    @Order(3)
    @SuppressWarnings("null")
    public void T_421_testStartInterview() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<InterviewStartRequestDTO> entity = new HttpEntity<>(
                new InterviewStartRequestDTO(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e")), headers);

        // When
        ResponseEntity<InterviewResponseDTO> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/start",
                HttpMethod.POST, entity, InterviewResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response2.getStatusCode());
        assertNotNull(response2.getBody());
        question = response2.getBody().getResponse();
    }

    @Test
    @Order(4)
    public void T_422_testStartInterview_InvalidStatus() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<InterviewStartRequestDTO> entity = new HttpEntity<>(
                new InterviewStartRequestDTO(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e")),
                headers);

        // When
        ResponseEntity<InterviewResponseDTO> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/start",
                HttpMethod.POST, entity, InterviewResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response2.getStatusCode());
    }

    @Test
    @Order(5)
    public void T_423_testStartInterview_WithRecruiter() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<InterviewStartRequestDTO> entity = new HttpEntity<>(
                new InterviewStartRequestDTO(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e")),
                headers);

        // When
        ResponseEntity<InterviewResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/start",
                HttpMethod.POST, entity, InterviewResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(6)
    @SuppressWarnings("null")
    public void T_431_testAnswerInterview() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        InterviewAnswerRequestDTO body = InterviewAnswerRequestDTO.builder()
                .jobApplicationId(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e"))
                .chat(InterviewChatDTO.builder()
                        .question(question)
                        .answer("answer")
                        .backspaceCount(0)
                        .build())
                .build();

        HttpEntity<InterviewAnswerRequestDTO> entity = new HttpEntity<>(
                body, headers);

        // When
        ResponseEntity<InterviewResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/answer",
                HttpMethod.PATCH, entity, InterviewResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        question = response.getBody().getResponse();
    }

    @Test
    @Order(7)
    public void T_432_testAnswerInterview_InvalidStatus() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        InterviewAnswerRequestDTO body = InterviewAnswerRequestDTO.builder()
                .jobApplicationId(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e"))
                .chat(InterviewChatDTO.builder()
                        .question(question)
                        .answer("answer")
                        .backspaceCount(0)
                        .build())
                .build();

        HttpEntity<InterviewAnswerRequestDTO> entity = new HttpEntity<>(
                body, headers);

        JobApplication application = jobApplicationRepository
                .findById(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e")).get();
        application.setStatus(JobApplicationStatus.EVALUATED);
        jobApplicationRepository.save(application);

        // When
        ResponseEntity<InterviewResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/answer",
                HttpMethod.PATCH, entity, InterviewResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());

        application.setStatus(JobApplicationStatus.INTERVIEW);
        jobApplicationRepository.save(application);
    }

    @Test
    @Order(8)
    public void T_433_testAnswerInterview_WithRecruiter() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        InterviewAnswerRequestDTO body = InterviewAnswerRequestDTO.builder()
                .jobApplicationId(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e"))
                .chat(InterviewChatDTO.builder()
                        .question(question)
                        .answer("answer")
                        .backspaceCount(0)
                        .build())
                .build();

        HttpEntity<InterviewAnswerRequestDTO> entity = new HttpEntity<>(
                body, headers);

        // When
        ResponseEntity<InterviewResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/answer",
                HttpMethod.PATCH, entity, InterviewResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(9)
    public void T_441_testUpdateInterviewScore() {
        // Given

        // Mocking the user
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);

        InterviewScoreRequestDTO body = InterviewScoreRequestDTO.builder()
                .jobApplicationId(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e"))
                .interviewScore(100.0f)
                .build();

        HttpEntity<InterviewScoreRequestDTO> entity = new HttpEntity<>(
                body, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/interview/score",
                HttpMethod.PATCH, entity, String.class);
        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(jobApplicationRepository.findById(UUID.fromString("5f52082b-1a23-4a5e-9d64-407529ea172e")).get().getInterviewScore());
    }
}
