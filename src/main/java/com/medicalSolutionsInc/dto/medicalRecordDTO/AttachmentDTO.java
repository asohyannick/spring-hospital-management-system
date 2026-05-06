package com.medicalSolutionsInc.dto.medicalRecordDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema (name = "Attachment", description = "Attached file details")
public record AttachmentDTO(
		String fileName,
		String fileUrl,
		String fileType,
		long fileSizeBytes,
		Instant uploadedAt
) {}