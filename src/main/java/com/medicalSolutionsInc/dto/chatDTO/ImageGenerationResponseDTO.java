package com.medicalSolutionsInc.dto.chatDTO;

import java.time.Instant;

public record ImageGenerationResponseDTO(
		String conversationId,
		String prompt,
		String imageUrl,
		Instant timestamp
) {}