package com.monitor.api.repository;

import com.monitor.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email); // Find user by email. Optional is a container object which may or may not contain a non-null value.
}
