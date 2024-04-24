package com.BE.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.BE.entities.CV;

@Repository
public interface CVRepository extends JpaRepository<CV, UUID> {
    public @NonNull Optional<CV> findById(@NonNull UUID id);

    public Optional<CV> findByFileName(String fileName);
}
