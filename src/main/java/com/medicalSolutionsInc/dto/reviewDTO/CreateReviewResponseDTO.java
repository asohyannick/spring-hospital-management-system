package com.medicalSolutionsInc.dto.reviewDTO;

import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;

import java.time.Instant;
import java.util.List;

public record CreateReviewResponseDTO(
		
		String id,
		
		String reviewNumber,
		
		String title,
		
		String comment,
		
		int rating,
		
		ReviewStatus status,
		
		String patientId,
		
		String patientName,
		
		String patientImageUrl,
		
		String targetId,
		
		String targetName,
		
		ReviewTargetType targetType,
		
		String referenceId,
		
		ReferenceType referenceType,
		
		int helpfulVotes,
		
		int reportedCount,
		
		List<String> tags,
		
		List<AttachmentDTO> attachments,
		
		ReviewResponseDTO response,
		
		boolean verifiedVisit,
		
		boolean anonymous,
		
		boolean featured,
		
		boolean edited,
		
		Instant editedAt,
		
		Instant createdAt,
		
		Instant updatedAt
) {}