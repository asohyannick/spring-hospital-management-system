package com.medicalSolutionsInc.dto.userDTO;
import jakarta.validation.constraints.NotBlank;
public record FirebaseLoginRequestDTO(
		@NotBlank (message = "Firebase ID token is required")
		String idToken
) {}
