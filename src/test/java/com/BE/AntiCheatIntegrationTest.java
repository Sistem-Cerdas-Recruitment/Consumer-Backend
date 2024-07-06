package com.BE;

import static org.junit.Assert.assertEquals;

import java.util.List;
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
import com.BE.dto.antiCheat.AntiCheatResultDTO;
import com.BE.dto.antiCheat.EvaluationDTO;
import com.BE.repositories.JobApplicationRepository;
import com.BE.security.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AntiCheatIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${service.x-api-key}")
    private String apiKey;

    @LocalServerPort
    private int port;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Test
    @Order(1)
    public void T_511_testAntiCheatEvaluation() {
        // Given

        // Mocking the user
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);

        List<EvaluationDTO> data = List.of(
                EvaluationDTO.builder()
                        .predictedClass("0")
                        .confidence("0.999")
                        .secondaryModelProbability("0.001")
                        .mainModelProbability("0.999")
                        .build(),
                EvaluationDTO.builder()
                        .predictedClass("1")
                        .confidence("0.999")
                        .secondaryModelProbability("0.001")
                        .mainModelProbability("0.999")
                        .build());

        AntiCheatResultDTO request = AntiCheatResultDTO.builder()
                .jobApplicationId(UUID.fromString("1cc0f4b9-206e-4809-b6f2-1252af69c9d4"))
                .evaluations(data)
                .build();

        HttpEntity<AntiCheatResultDTO> entity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/anti-cheat/result",
                HttpMethod.PATCH, entity,
                String.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        jobApplicationRepository.findById(UUID.fromString("1cc0f4b9-206e-4809-b6f2-1252af69c9d4"))
                .ifPresent(jobApplication -> {
                    jobApplication.setStatus(JobApplicationStatus.INTERVIEW);
                    jobApplicationRepository.save(jobApplication);
                });
    }
}
