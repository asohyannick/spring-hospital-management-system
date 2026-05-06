package com.medicalSolutionsInc.repository.review;

import com.medicalSolutionsInc.entity.review.Review;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
			
			Page<Review> findByDeletedAtIsNull(Pageable pageable);
			Optional<Review> findByIdAndDeletedAtIsNull(String id);
			long countByDeletedAtIsNull();
			
			@Query("""
			            {
			              "deleted_at": null,
			              "$and": [
			                { "$or": [
			                  { "$expr": { "$eq": [{ "$type": "?0" }, "missing"] } },
			                  { "title":        { "$regex": "?0", "$options": "i" } },
			                  { "comment":      { "$regex": "?0", "$options": "i" } },
			                  { "patient_name": { "$regex": "?0", "$options": "i" } },
			                  { "target_name":  { "$regex": "?0", "$options": "i" } },
			                  { "tags":         { "$regex": "?0", "$options": "i" } }
			                ]},
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?1" }, "missing"] } }, { "target_type": "?1" }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?2" }, "missing"] } }, { "target_id": "?2" }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?3" }, "missing"] } }, { "patient_id": "?3" }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?4" }, "missing"] } }, { "status": "?4" }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?5" }, "missing"] } }, { "rating": { "$gte": ?5 } }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?6" }, "missing"] } }, { "rating": { "$lte": ?6 } }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?7" }, "missing"] } }, { "verified_visit": ?7 }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?8" }, "missing"] } }, { "featured": ?8 }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?9" }, "missing"] } }, { "created_at": { "$gte": "?9" } }] },
			                { "$or": [{ "$expr": { "$eq": [{ "$type": "?10" }, "missing"] } }, { "created_at": { "$lte": "?10" } }] }
			              ]
			            }
			            """)
			Page<Review> search(
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