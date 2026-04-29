package com.medicalSolutionsInc.dto.userDTO;

import jakarta.validation.constraints.*;
public record MagicLinkTokenRequestDTO(
		@NotBlank(message = "Email is required")
		@Email(message = "Email must be valid")
		String email
) {}
