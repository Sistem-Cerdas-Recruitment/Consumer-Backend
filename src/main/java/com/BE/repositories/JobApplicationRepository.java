package com.BE.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.repositories.projections.JobApplicationProjection.JobApplicationUserJobProjection;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    Optional<JobApplication> findByJobAndUser(Job job, User user);

    List<JobApplicationUserJobProjection> findAllByUser(User user);

    List<JobApplication> findByJob(Job job);
}