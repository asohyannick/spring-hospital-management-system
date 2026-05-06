package com.medicalSolutionsInc.dto.reviewDTO;

import java.time.Instant;

public record ReviewResponseDTO(
		String responderId,
		String responderName,
		String responderRole,
		String message,
		Instant respondedAt,
		boolean edited,
		Instant editedAt
) {}