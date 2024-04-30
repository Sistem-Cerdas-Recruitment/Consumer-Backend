package com.BE.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BE.entities.JobApplication;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    Optional<JobApplication> findByJobIdAndUserId(UUID jobId, UUID userId);
}