package com.medicalSolutionsInc.dto.patientProfileDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Schema (name = "Insurance", description = "Health insurance details")
public record InsuranceDTO(
		
		@Schema(description = "Insurance provider name", example = "AXA Health")
		@NotBlank (message = "Insurance provider name is required")
		String providerName,
		
		@Schema(description = "Policy number", example = "POL-2026-00123")
		@NotBlank(message = "Policy number is required")
		String policyNumber,
		
		@Schema(description = "Group number", example = "GRP-001")
		String groupNumber,
		
		@Schema(description = "Coverage type", example = "Full")
		String coverageType,
		
		@Schema(description = "Insurance expiry date")
		Instant expiryDate,
		
		@Schema(description = "Whether the insurance is currently active", example = "true")
		boolean active
) {}