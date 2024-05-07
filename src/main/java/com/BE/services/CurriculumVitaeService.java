package com.BE.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

        if(cvs.isEmpty()) {
            cv.setDefault(true);
        }

        return curriculumVitaeRepository.save(cv);
    }

}
