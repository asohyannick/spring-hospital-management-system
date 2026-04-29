package com.medicalSolutionsInc.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResentMagicLinkTokenRequestDTO(
		@NotBlank(message = "Email address must be provided")
		@NotNull(message = "Email cannot be blank")
	    String email
) {}
