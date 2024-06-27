package com.BE.services;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.BE.constants.EndpointConstants;
import com.BE.entities.CurriculumVitae;
import com.BE.entities.User;
import com.BE.repositories.CurriculumVitaeRepository;
import com.BE.services.storage.BucketService;
import com.BE.services.user.UserService;

@Service
public class CurriculumVitaeService {

    @Value("${service.x-api-key}")
    private String xApiKey;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    CurriculumVitaeRepository curriculumVitaeRepository;

    @Autowired
    BucketService bucketService;

    public CurriculumVitae find(UUID id, User user) {
        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CV not found"));
        if (cv.getUser().getId().equals(user.getId())) {
            return cv;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public String get(UUID id, String username) {
        // User user = userService.getUserByEmail(username);
        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CV not found"));

        if (cv.getUser().getUsername().equals(username)) {
            return bucketService.createPresignedGetUrl(cv.getFileName());
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }

    }

    public List<CurriculumVitae> get(String username) {
        User user = userService.getUserByEmail(username);
        return curriculumVitaeRepository.findAllByUser(user,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "isDefault", "createdAt")));
    }

    public byte[] view(UUID id, String username) {
        User user = userService.getUserByEmail(username);
        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CV not found"));
        if (cv.getUser().getId().equals(user.getId())) {
            return bucketService.get(cv.getFileName());
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public CurriculumVitae save(MultipartFile file, String username) {
        User user = userService.getUserByEmail(username);
        String fileName = bucketService.put(file, user.getName());
        List<CurriculumVitae> cvs = curriculumVitaeRepository.findAllByUser(user,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "isDefault", "createdAt")));

        CurriculumVitae cv = CurriculumVitae.builder()
                .originalFileName(file.getOriginalFilename())
                .fileName(fileName)
                .user(user)
                .build();

        if (cvs.isEmpty()) {
            cv.setDefault(true);
        }

        return curriculumVitaeRepository.save(cv);
    }

    public CurriculumVitae setDefault(UUID cvId, String username) {
        User user = userService.getUserByEmail(username);
        List<CurriculumVitae> cvs = curriculumVitaeRepository.findAllByUser(user,
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "isDefault", "createdAt")));
        CurriculumVitae curriculumVitae = null;

        for (CurriculumVitae cv : cvs) {
            if (cv.getId().equals(cvId)) {
                cv.setDefault(true);
                curriculumVitae = cv;
            } else {
                cv.setDefault(false);
            }
        }

        curriculumVitaeRepository.saveAll(cvs);

        return curriculumVitae;
    }

    public ResponseEntity<Object> extract(UUID id) {
        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CV not found"));
        String cvUrl = bucketService.createPresignedGetUrl(cv.getFileName());
        Map<String, String> body = Map.of("link", cvUrl);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, getHeaders());
        ResponseEntity<Object> response = restTemplate.postForEntity(EndpointConstants.MATCHING_SERVICE + "/cv", entity,
                Object.class);
        return response;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", xApiKey);
        return headers;
    }

}
