package com.BE.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CurriculumVitae cv = curriculumVitaeService.save(file, username);
        FileResponseDTO body = FileResponseDTO.builder()
                .id(cv.getId())
                .fileName(cv.getOriginalFileName())
                .uploadDate(cv.getCreatedAt())
                .build();
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/cv/default")
    public ResponseEntity<Object> setDefaultCV(@RequestBody UUID id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CurriculumVitae cv = curriculumVitaeService.setDefault(id, username);
        FileResponseDTO body =  FileResponseDTO.builder()
                .id(cv.getId())
                .fileName(cv.getOriginalFileName())
                .uploadDate(cv.getCreatedAt())
                .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = { "/cv/get", "/cv/get/{id}" })
    public ResponseEntity<Object> getCV(@PathVariable Optional<String> id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (id.isPresent()) {
            String cvUrl = curriculumVitaeService.get(UUID.fromString(id.get()), username);
            Map<String, String> body = new HashMap<>();
            body.put("data", cvUrl);
            return ResponseEntity.ok(body);
        } else {
            List<CurriculumVitae> cvPage = curriculumVitaeService.get(username);
            MultipleFileResponseDTO body = new MultipleFileResponseDTO(cvPage);
            return ResponseEntity.ok(body);
        }
    }

    @GetMapping("/cv/view/{id}")
    public ResponseEntity<Object> viewCV(@PathVariable UUID id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        byte[] content = curriculumVitaeService.view(id, username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(content);
    }

    @PostMapping("/cv/extract")
    public ResponseEntity<Object> extractCV(@RequestBody Map<String, UUID> request) {
        return curriculumVitaeService.extract(request.get("id"));
    }
}
