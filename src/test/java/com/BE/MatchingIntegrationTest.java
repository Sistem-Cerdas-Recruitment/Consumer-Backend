package com.BE;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
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

import com.BE.dto.job.matching.RelevanceUpdateRequestDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatchingIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${service.x-api-key}")
    private String apiKey;

    @LocalServerPort
    private int port;

    @Test
    public void T_611_testUpdateMatching() {
        // Given

        // Mocking the user
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);

        RelevanceUpdateRequestDTO request = RelevanceUpdateRequestDTO.builder()
                .jobApplicationId(UUID.fromString("1cc0f4b9-206e-4809-b6f2-1252af69c9d4"))
                .relevanceScore(0.5)
                .isRelevant(true)
                .build();

        HttpEntity<RelevanceUpdateRequestDTO> entity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/matching",
                HttpMethod.PATCH, entity,
                String.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    }
}
