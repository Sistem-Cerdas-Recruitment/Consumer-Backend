package com.BE.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.BE.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public @NonNull Optional<User> findById(@NonNull UUID id);

    public Optional<User> findByName(String name);
    public Optional<User> findByEmail(String email);
}
