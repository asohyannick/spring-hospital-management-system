package com.medicalSolutionsInc.dto.medicalRecordDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Schema (name = "LabResult", description = "Laboratory test result details")
public record LabResultDTO(
		
		@Schema(description = "Test name", example = "Complete Blood Count")
		@NotBlank (message = "Test name is required")
		String testName,
		
		@Schema(description = "Test result value", example = "13.5")
		@NotBlank(message = "Result is required")
		String result,
		
		@Schema(description = "Unit of measurement", example = "g/dL")
		String unit,
		
		@Schema(description = "Normal reference range", example = "12.0 - 16.0")
		String referenceRange,
		
		@Schema(description = "Whether the result is abnormal", example = "false")
		boolean abnormal,
		
		@Schema(description = "Time the test was conducted")
		Instant testedAt,
		
		@Schema(description = "Laboratory ID reference", example = "664f1b2e3a4b5c6d7e8f9a0e")
		String laboratoryId
) {}