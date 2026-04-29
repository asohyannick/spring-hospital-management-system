package com.medicalSolutionsInc.dto.userDTO;
import com.medicalSolutionsInc.enumerations.user.UserRole;

import java.time.Instant;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        boolean active,
        boolean accountBlocked,
        String accessToken,
        String refreshToken,
        Instant createdAt,
        Instant updatedAt
) {
}
