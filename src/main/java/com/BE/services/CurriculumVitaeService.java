package com.BE.services;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.BE.entities.CurriculumVitae;
import com.BE.entities.User;
import com.BE.repositories.CurriculumVitaeRepository;
import com.BE.services.storage.BucketService;

@Service
public class CurriculumVitaeService {

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
        User user = userService.getUserByEmail(username);
        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CV not found"));

        if (cv.getUser().getId().equals(user.getId())) {
            return bucketService.createPresignedGetUrl(cv.getFileName());
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }

    }

    public Page<CurriculumVitae> get(String username) {
        User user = userService.getUserByEmail(username);
        return curriculumVitaeRepository.findAllByUser(user,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
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

        CurriculumVitae cv = CurriculumVitae.builder()
                .originalFileName(file.getOriginalFilename())
                .fileName(fileName)
                .user(user)
                .build();

        return curriculumVitaeRepository.save(cv);
    }

}
