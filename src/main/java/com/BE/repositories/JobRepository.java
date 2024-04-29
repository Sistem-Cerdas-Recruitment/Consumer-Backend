package com.BE.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.BE.entities.Job;

public interface JobRepository extends JpaRepository<Job, UUID> {

    public @NonNull Page<Job> findAll(@NonNull Pageable pageable);

    public @NonNull List<Job> findAll();

    public @NonNull Page<Job> findAllByTitleContaining(String title, Pageable pageable);
}
