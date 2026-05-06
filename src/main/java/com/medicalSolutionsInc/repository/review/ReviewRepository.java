package com.medicalSolutionsInc.repository.review;

import com.medicalSolutionsInc.entity.review.Review;
import com.medicalSolutionsInc.utils.reviewRepositoryCustom.ReviewRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String>, ReviewRepositoryCustom {
			
			Page<Review> findByDeletedAtIsNull(Pageable pageable);
			Optional<Review> findByIdAndDeletedAtIsNull(String id);
			long countByDeletedAtIsNull();
			
}