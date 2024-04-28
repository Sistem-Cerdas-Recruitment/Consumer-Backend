package com.BE.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.BE.entities.CurriculumVitae;

@Repository
public interface CurriculumVitaeRepository extends JpaRepository<CurriculumVitae, UUID> {
    public @NonNull Optional<CurriculumVitae> findById(@NonNull UUID id);

    public Optional<CurriculumVitae> findByFileName(String fileName);
}
