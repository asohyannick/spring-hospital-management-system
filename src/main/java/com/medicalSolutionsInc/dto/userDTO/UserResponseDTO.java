package com.medicalSolutionsInc.dto.userDTO;
import com.medicalSolutionsInc.enumerations.user.UserRole;

import java.time.Instant;

public record UserResponseDTO(
        String id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        boolean active,
        boolean accountBlocked,
        Instant createdAt,
        Instant updatedAt
) {
}
