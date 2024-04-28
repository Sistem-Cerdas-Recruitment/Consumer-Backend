package com.BE.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.BE.entities.CurriculumVitae;
import com.BE.entities.User;
import com.BE.repositories.CurriculumVitaeRepository;

@Service
public class CurriculumVitaeService {

    @Autowired
    CurriculumVitaeRepository curriculumVitaeRepository;

    public CurriculumVitae get(UUID id, User user) {
        CurriculumVitae cv = curriculumVitaeRepository.findById(id).orElse(null);
        if (cv != null) {
            if (cv.getUser().getId() == user.getId() || cv.getJob().getUser().getId() == user.getId()) {
                return cv;
            } else {
                throw new AccessDeniedException("You are not authorized to access this resource");
            }
        }
        return cv;
    }

    public CurriculumVitae save(CurriculumVitae cv) {
        return curriculumVitaeRepository.save(cv);
    }

}
