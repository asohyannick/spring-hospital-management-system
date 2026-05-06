package com.medicalSolutionsInc.repository.review;

import com.medicalSolutionsInc.entity.review.Review;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

		Optional<Review> findByReviewNumber(String reviewNumber);
		
		boolean existsByReviewNumber(String reviewNumber);
		
		Page<Review> findByPatientId(String patientId, Pageable pageable);
		
		List<Review> findByPatientIdAndStatus(String patientId, ReviewStatus status);
		
		Page<Review> findByTargetId(String targetId, Pageable pageable);
		
		Page<Review> findByTargetIdAndStatus(String targetId, ReviewStatus status, Pageable pageable);
		
		Page<Review> findByTargetIdAndTargetType(String targetId, ReviewTargetType targetType, Pageable pageable);
		
		long countByTargetIdAndStatus(String targetId, ReviewStatus status);
		
		Page<Review> findByStatus(ReviewStatus status, Pageable pageable);
		
		List<Review> findByFeaturedTrue();
		
		List<Review> findByReportedCountGreaterThan(int threshold);
		
		@Query("{ 'target_id': ?0, 'status': 'APPROVED', 'deleted_at': null }")
		Page<Review> findApprovedByTargetId(String targetId, Pageable pageable);
		
		@Query("{ 'deleted_at': null }")
		Page<Review> findAllActive(Pageable pageable);
		
		@Query(value = "{ 'target_id': ?0, 'status': 'APPROVED', 'deleted_at': null }",
				fields = "{ 'rating': 1 }")
		List<Review> findRatingsByTargetId(String targetId);
}