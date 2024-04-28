package com.BE.services;

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
    CurriculumVitaeRepository curriculumVitaeRepository;

    @Autowired
    BucketService bucketService;

    public String get(UUID id, User user) {
        CurriculumVitae cv = curriculumVitaeRepository.findById(id).orElse(null);
        if (cv != null) {
            if (cv.getUser().getId().equals(user.getId())) {
                return bucketService.createPresignedGetUrl(cv.getFileName());
            } else {
                throw new AccessDeniedException("You are not authorized to access this resource");
            }
        }
        return null;
    }

    public byte[] view(UUID id, User user) {
        CurriculumVitae cv = curriculumVitaeRepository.findById(id).orElse(null);
        if(cv != null){
            if (cv.getUser().getId().equals(user.getId())) {
                return bucketService.get(cv.getFileName());
            } else{
                throw new AccessDeniedException("You are not authorized to access this resource");
            }
        }
        return null;
    }

    public Page<CurriculumVitae> get(User user) {
        return curriculumVitaeRepository.findAllByUser(user, PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "createdAt")));
    }

    public CurriculumVitae save(MultipartFile file, User user) {
        String fileName = bucketService.put(file, user.getName());
        
        CurriculumVitae cv = CurriculumVitae.builder()
                .originalFileName(file.getOriginalFilename())
                .fileName(fileName)
                .user(user)
                .build();

        return curriculumVitaeRepository.save(cv);
    }

}
