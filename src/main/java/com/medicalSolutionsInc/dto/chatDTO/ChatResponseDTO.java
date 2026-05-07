package com.medicalSolutionsInc.dto.chatDTO;

import java.time.Instant;

public record ChatResponseDTO(
		String conversationId,
		String reply,
		Instant timestamp
) {}