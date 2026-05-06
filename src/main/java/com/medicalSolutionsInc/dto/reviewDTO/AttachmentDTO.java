package com.medicalSolutionsInc.dto.reviewDTO;

import java.time.Instant;

public record AttachmentDTO(
		String fileName,
		String fileUrl,
		String fileType,
		long fileSizeBytes,
		Instant uploadedAt
) {}