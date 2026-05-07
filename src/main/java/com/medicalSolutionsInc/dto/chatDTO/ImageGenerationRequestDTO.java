package com.medicalSolutionsInc.dto.chatDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ImageGenerationRequestDTO(
		@NotBlank (message = "Prompt is required")
		String prompt,
		
		String model,
		
		@Min (256)
		@Max (1024)
		int width,
		
		@Min(256)
		@Max(1024)
		int height
) {}