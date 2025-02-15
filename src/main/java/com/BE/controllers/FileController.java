package com.BE.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.BE.dto.CurriculumVitaeRequestDTO;
import com.BE.dto.file.FileResponseDTO;
import com.BE.dto.file.MultipleFileResponseDTO;
import com.BE.dto.file.URLResponseDTO;
import com.BE.entities.CurriculumVitae;
import com.BE.services.CurriculumVitaeService;
import com.BE.services.storage.BucketService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private CurriculumVitaeService curriculumVitaeService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String response = bucketService.put(file, username);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/getFileUrl")
    public ResponseEntity<Object> getFileUrl(@RequestBody String fileName) {
        return ResponseEntity.ok(bucketService.createPresignedGetUrl(fileName));
    }

    // @SuppressWarnings("null")
    @PostMapping("/cv/upload")
    @RolesAllowed("CANDIDATE")
    public ResponseEntity<Object> uploadCV(@RequestParam("file") MultipartFile file) {

        String originalFileName = file.getOriginalFilename();

        if (file.getOriginalFilename() == null) {
            throw new InvalidFileNameException(file.getOriginalFilename(),
                    "Invalid file format. Only PDF files are allowed.");
        } else if (originalFileName != null) {
            if (!originalFileName.endsWith(".pdf")) {
                throw new InvalidFileNameException(file.getOriginalFilename(),
                        "Invalid file format. Only PDF files are allowed.");
            }
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
    @RolesAllowed("CANDIDATE")
    public ResponseEntity<Object> setDefaultCV(@RequestBody @Validated CurriculumVitaeRequestDTO request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CurriculumVitae cv = curriculumVitaeService.setDefault(request.getId(), username);
        FileResponseDTO body = FileResponseDTO.builder()
                .id(cv.getId())
                .fileName(cv.getOriginalFileName())
                .uploadDate(cv.getCreatedAt())
                .isDefault(cv.isDefault())
                .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = { "/cv/get", "/cv/get/{id}" })
    public ResponseEntity<Object> getCV(@PathVariable Optional<String> id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (id.isPresent()) {
            String cvUrl = curriculumVitaeService.get(UUID.fromString(id.get()), username);
            return ResponseEntity.ok(new URLResponseDTO(cvUrl));
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
    @RolesAllowed("CANDIDATE")
    public ResponseEntity<Object> extractCV(@RequestBody @Validated CurriculumVitaeRequestDTO request) {
        return curriculumVitaeService.extract(request.getId());
    }
}
