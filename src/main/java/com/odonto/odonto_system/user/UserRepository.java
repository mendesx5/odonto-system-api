package com.odonto.odonto_system.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Page<User> findByRoleAndActive(UserRole role, boolean active, Pageable pageable);
    Page<User> findByRole(UserRole role, Pageable pageable);
    Page<User> findByActive(boolean active, Pageable pageable);

}
