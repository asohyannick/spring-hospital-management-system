package com.medicalSolutionsInc.dto.staffProfileDTO;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record CertificationDTO(
		@NotBlank String name,
		String issuingBody,
		String certificateNumber,
		Instant issuedAt,
		Instant expiresAt,
		boolean active
) {}