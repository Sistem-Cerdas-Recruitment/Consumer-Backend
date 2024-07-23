package com.BE;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.BE.constants.Role;
import com.BE.dto.auth.AuthRequestDTO;
import com.BE.dto.auth.RegisterRequestDTO;
import com.BE.entities.User;
import com.BE.security.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @Test
    @Order(1)
    public void T_711_testRegister() {

        // Given
        HttpHeaders headers = new HttpHeaders();

        RegisterRequestDTO body = RegisterRequestDTO.builder()
                .email("testUser@mail.com")
                .name("Test User")
                .password("password")
                .role("CANDIDATE")
                .build();

        HttpEntity<RegisterRequestDTO> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:" + port + "/api/auth/register",
                HttpMethod.POST, entity, Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    @Order(2)
    public void T_712_testRegister_MissingField() {

        // Given
        HttpHeaders headers = new HttpHeaders();

        RegisterRequestDTO body = RegisterRequestDTO.builder()
                .email("testUser@mail.com")
                .name("Test User")
                .password("password")
                .build();

        HttpEntity<RegisterRequestDTO> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:" + port + "/api/auth/register",
                HttpMethod.POST, entity, Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @Order(3)
    public void T_721_testLogin() {

        // Given
        HttpHeaders headers = new HttpHeaders();

        AuthRequestDTO body = AuthRequestDTO.builder()
                .email("testUser@mail.com")
                .password("password")
                .build();

        HttpEntity<AuthRequestDTO> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:" + port + "/api/auth/login",
                HttpMethod.POST, entity, Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    @Order(4)
    public void T_722_testLogin_InvalidCredentials() {

        // Given
        HttpHeaders headers = new HttpHeaders();

        AuthRequestDTO body = AuthRequestDTO.builder()
                .email("testUser@mail.com")
                .build();

        HttpEntity<AuthRequestDTO> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:" + port + "/api/auth/login",
                HttpMethod.POST, entity, Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @Order(5)
    public void T_731_testRefresh() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest1@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // When
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:" + port + "/api/auth/refresh",
                HttpMethod.POST, new HttpEntity<>(headers), Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}
