package com.medicalSolutionsInc.utils.reviewRepositoryCustom;

import com.medicalSolutionsInc.entity.review.Review;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
		
		private final MongoTemplate mongoTemplate;
		
		@Override
		public Page < Review > search(
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
		) {
			Criteria criteria = Criteria.where("deletedAt").isNull();
			
			if ( StringUtils.hasText(keyword)) {
				Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
				criteria.andOperator(
						new Criteria().orOperator(
								Criteria.where("title").regex(pattern),
								Criteria.where("comment").regex(pattern),
								Criteria.where("patientName").regex(pattern),
								Criteria.where("targetName").regex(pattern),
								Criteria.where("tags").regex(pattern)
						)
				);
			}
			
			if (targetType != null)    criteria.and("targetType").is(targetType);
			if (targetId != null)      criteria.and("targetId").is(targetId);
			if (patientId != null)     criteria.and("patientId").is(patientId);
			if (status != null)        criteria.and("status").is(status);
			if (verifiedVisit != null) criteria.and("verifiedVisit").is(verifiedVisit);
			if (featured != null)      criteria.and("featured").is(featured);
			
			if (minRating != null && maxRating != null) {
				criteria.and("rating").gte(minRating).lte(maxRating);
			} else if (minRating != null) {
				criteria.and("rating").gte(minRating);
			} else if (maxRating != null) {
				criteria.and("rating").lte(maxRating);
			}
			
			if (from != null && to != null) {
				criteria.and("createdAt").gte(from).lte(to);
			} else if (from != null) {
				criteria.and("createdAt").gte(from);
			} else if (to != null) {
				criteria.and("createdAt").lte(to);
			}
			
			Query query = new Query(criteria).with(pageable);
			
			List <Review> results = mongoTemplate.find(query, Review.class);
			long total = mongoTemplate.count( Query.of(query).limit(-1).skip(-1), Review.class);
			
			return new PageImpl <> (results, pageable, total);
		}
}