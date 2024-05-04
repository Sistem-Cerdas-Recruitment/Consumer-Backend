package com.BE.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.BE.dto.file.FileResponseDTO;
import com.BE.dto.file.MultipleFileResponseDTO;
import com.BE.entities.CurriculumVitae;
import com.BE.entities.User;
import com.BE.services.CurriculumVitaeService;
import com.BE.services.storage.BucketService;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private CurriculumVitaeService curriculumVitaeService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getName();

        try {
            String response = bucketService.put(file, username);
            return response;
        } catch (Exception e) {
            return "Failed to upload file: " + e;
        }

    }

    @PostMapping("/getFileUrl")
    public String getFileUrl(@RequestBody String fileName) {
        return bucketService.createPresignedGetUrl(fileName);
    }

    @SuppressWarnings("null")
    @PostMapping("/cv/upload")
    public ResponseEntity<Object> uploadCV(@RequestParam("file") MultipartFile file) {

        if(file.getOriginalFilename() == null || !file.getOriginalFilename().endsWith(".pdf")){
            throw new InvalidFileNameException(file.getOriginalFilename(), "Invalid file format. Only PDF files are allowed.");
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CurriculumVitae cv = curriculumVitaeService.save(file, user);
        FileResponseDTO body = FileResponseDTO.builder()
                .id(cv.getId())
                .fileName(cv.getOriginalFileName())
                .uploadDate(cv.getCreatedAt())
                .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = { "/cv/get", "/cv/get/{id}" })
    public ResponseEntity<Object> getCV(@PathVariable Optional<String> id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (id.isPresent()) {
            String cvUrl = curriculumVitaeService.get(UUID.fromString(id.get()), user);
            Map<String, String> body = new HashMap<>();
            body.put("data", cvUrl);
            return ResponseEntity.ok(body);
        } else {
            Page<CurriculumVitae> cvPage = curriculumVitaeService.get(user);
            MultipleFileResponseDTO body = new MultipleFileResponseDTO(cvPage);
            return ResponseEntity.ok(body);
        }
    }

    @GetMapping("/cv/view/{id}")
    public ResponseEntity<Object> viewCV(@PathVariable UUID id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        byte[] content = curriculumVitaeService.view(id, user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(content);
    }
}
