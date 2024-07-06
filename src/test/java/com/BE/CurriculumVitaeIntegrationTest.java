package com.BE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.BE.constants.Role;
import com.BE.dto.file.FileResponseDTO;
import com.BE.dto.file.MultipleFileResponseDTO;
import com.BE.dto.file.URLResponseDTO;
import com.BE.entities.User;
import com.BE.repositories.CurriculumVitaeRepository;
import com.BE.security.JwtService;
import com.BE.services.CurriculumVitaeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CurriculumVitaeIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CurriculumVitaeService curriculumVitaeService;

    @Autowired
    private CurriculumVitaeRepository curriculumVitaeRepository;

    private static UUID cvId;

    @Test
    @Order(1)
    @SuppressWarnings("null")
    public void T_311_testGetRecentCV() {
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
        ResponseEntity<MultipleFileResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/get",
                org.springframework.http.HttpMethod.GET,
                entity,
                MultipleFileResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1, response.getBody().getFiles().size());
    }

    @Test
    @Order(2)
    public void T_321_testGetCV() {
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
        ResponseEntity<URLResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/get/{id}",
                org.springframework.http.HttpMethod.GET,
                entity,
                URLResponseDTO.class,
                "2f1b1f18-e26c-442f-938e-90730b0d125d");

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    @Order(3)
    public void T_322_testGetCV_WithOtherUser() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<URLResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/get/{id}",
                org.springframework.http.HttpMethod.GET,
                entity,
                URLResponseDTO.class,
                "2f1b1f18-e26c-442f-938e-90730b0d125d");

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(4)
    @SuppressWarnings("null")
    public void T_331_testUploadCV() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
        byte[] bytes = new byte[1024];
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.TEXT_PLAIN_VALUE, bytes);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<FileResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/upload",
                org.springframework.http.HttpMethod.POST,
                requestEntity,
                FileResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody().getId());
        cvId = response.getBody().getId();
    }

    @Test
    @Order(5)
    public void T_332_testUploadCV_SizeLimit() {
        // Cleanup
        curriculumVitaeService.delete(cvId);
        assertEquals(2, curriculumVitaeRepository.count());

        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
        byte[] bytes = new byte[1024 * 1024 * 5];
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.TEXT_PLAIN_VALUE, bytes);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<FileResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/upload",
                org.springframework.http.HttpMethod.POST,
                requestEntity,
                FileResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @Order(6)
    public void T_333_testUploadCV_MissingFile() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<FileResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/upload",
                org.springframework.http.HttpMethod.POST,
                requestEntity,
                FileResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }
    
    @Test
    @Order(7)
    public void T_334_testUploadCV_InvalidFileType() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest2@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
        byte[] bytes = new byte[5];
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, bytes);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<FileResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/upload",
                org.springframework.http.HttpMethod.POST,
                requestEntity,
                FileResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @Order(8)
    public void T_335_testUploadCV_WithRecruiter() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
        byte[] bytes = new byte[5];
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.TEXT_PLAIN_VALUE, bytes);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<FileResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/upload",
                org.springframework.http.HttpMethod.POST,
                requestEntity,
                FileResponseDTO.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    @Order(9)
    public void T_341_testExtractCV() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("candidateTest1@mail.com")
                .role(Role.CANDIDATE)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Map<String, UUID> body = Map.of("id", UUID.fromString("2f1b1f18-e26c-442f-938e-90730b0d125d"));

        HttpEntity<Map<String, UUID>> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/extract",
                org.springframework.http.HttpMethod.POST,
                entity,
                Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(10)
    public void T_342_testExtractCV_WithRecruiter() {
        // Given

        // Mocking the user
        User user = User.builder()
                .email("recruiterTest1@mail.com")
                .role(Role.RECRUITER)
                .build();
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Map<String, UUID> body = Map.of("id", UUID.fromString("2f1b1f18-e26c-442f-938e-90730b0d125d"));

        HttpEntity<Map<String, UUID>> entity = new HttpEntity<>(body, headers);

        // When
        ResponseEntity<Object> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/file/cv/extract",
                org.springframework.http.HttpMethod.POST,
                entity,
                Object.class);

        // Then
        assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }
}
