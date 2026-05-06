package com.medicalSolutionsInc.dto.reviewDTO;

import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateReviewRequestDTO(
		
		String title,
		
		@NotBlank(message = "Comment is required")
		String comment,
		
		@Min(value = 1, message = "Rating must be at least 1")
		@Max(value = 5, message = "Rating must be at most 5")
		int rating,
		
		@NotBlank(message = "Patient ID is required")
		String patientId,
		
		String patientName,
		
		String patientImageUrl,
		
		@NotBlank(message = "Target ID is required")
		String targetId,
		
		String targetName,
		
		@NotNull(message = "Target type is required")
		ReviewTargetType targetType,
		
		String referenceId,
		
		ReferenceType referenceType,
		
		List<String> tags,
		
		boolean verifiedVisit,
		
		boolean anonymous
) {}