package com.medicalSolutionsInc.dto.chatDTO;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDTO(
		@NotBlank (message = "Message is required")
		String message,
		
		String conversationId
) {}