package com.medicalSolutionsInc.utils.reviewRepositoryCustom;

import com.medicalSolutionsInc.entity.review.Review;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface ReviewRepositoryCustom {
Page < Review > search(
		String keyword,
		ReviewTargetType targetType,
		String targetId,
		String patientId,
		ReviewStatus status,
		Integer minRating,
		Integer maxRating,
		Boolean verifiedVisit,
		Boolean featured,
		Instant from,
		Instant to,
		Pageable pageable
);
}