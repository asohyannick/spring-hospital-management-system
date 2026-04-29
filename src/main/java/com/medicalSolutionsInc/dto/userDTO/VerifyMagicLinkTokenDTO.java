package com.medicalSolutionsInc.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
public record VerifyMagicLinkTokenDTO(
		@NotBlank(message = "Token must be provided")
		String token
) { }