package com.BE.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.BE.constants.JobStatus;
import com.BE.entities.Job;
import com.BE.entities.User;

public interface JobRepository extends JpaRepository<Job, UUID> {

    public @NonNull Page<Job> findAll(@NonNull Pageable pageable);

    public List<Job> findAllByStatus(JobStatus status, Pageable pageable);

    public List<Job> findAllByUser(User user, Pageable pageable);

    public @NonNull Page<Job> findAllByTitleContaining(String title, Pageable pageable);
}
