package com.BE.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.BE.entities.Job;

public interface JobRepository extends JpaRepository<Job, UUID> {

    public @NonNull List<Job> findAll();

    // public @NonNull List<Job> findByTitleContaining(String title, Pageable pageable);
}
