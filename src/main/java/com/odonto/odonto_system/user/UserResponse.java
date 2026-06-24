package com.odonto.odonto_system.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        UserRole role,
        boolean active,
        LocalDateTime createdAt
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
