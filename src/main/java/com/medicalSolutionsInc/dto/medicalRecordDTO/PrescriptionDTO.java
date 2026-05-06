package com.medicalSolutionsInc.dto.medicalRecordDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Schema (name = "Prescription", description = "Prescription details")
public record PrescriptionDTO(
		
		@Schema(description = "Medication name", example = "Amoxicillin")
		@NotBlank (message = "Medication name is required")
		String medicationName,
		
		@Schema(description = "Dosage", example = "500mg")
		@NotBlank(message = "Dosage is required")
		String dosage,
		
		@Schema(description = "Frequency", example = "Twice daily")
		@NotBlank(message = "Frequency is required")
		String frequency,
		
		@Schema(description = "Duration", example = "7 days")
		@NotBlank(message = "Duration is required")
		String duration,
		
		@Schema(description = "Special instructions", example = "Take after meals")
		String instructions,
		
		@Schema(description = "Time the prescription was issued")
		Instant prescribedAt
) {}