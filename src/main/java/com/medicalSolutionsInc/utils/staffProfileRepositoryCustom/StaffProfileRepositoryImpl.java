package com.medicalSolutionsInc.utils.staffProfileRepositoryCustom;

import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
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
public class StaffProfileRepositoryImpl implements StaffProfileRepositoryCustom {

		private final MongoTemplate mongoTemplate;
		
		@Override
		public Page < StaffProfile > search(
				String keyword,
				StaffRole role,
				GenderType gender,
				EmploymentStatus employmentStatus,
				String department,
				String facilityId,
				Boolean verified,
				Boolean available,
				Instant from,
				Instant to,
				Pageable pageable
		) {
			Criteria criteria = Criteria.where("deletedAt").isNull();
			
			if ( StringUtils.hasText(keyword)) {
				Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
				criteria.andOperator(
						new Criteria().orOperator(
								Criteria.where("firstName").regex(pattern),
								Criteria.where("lastName").regex(pattern),
								Criteria.where("email").regex(pattern),
								Criteria.where("phoneNumber").regex(pattern),
								Criteria.where("staffNumber").regex(pattern),
								Criteria.where("licenseNumber").regex(pattern),
								Criteria.where("department").regex(pattern),
								Criteria.where("specialization").regex(pattern)
						)
				);
			}
			
			if (role != null)             criteria.and("role").is(role);
			if (gender != null)           criteria.and("gender").is(gender);
			if (employmentStatus != null) criteria.and("employmentStatus").is(employmentStatus);
			if (department != null)       criteria.and("department").is(department);
			if (facilityId != null)       criteria.and("facilityId").is(facilityId);
			if (verified != null)         criteria.and("verified").is(verified);
			if (available != null)        criteria.and("available").is(available);
			
			if (from != null && to != null) {
				criteria.and("createdAt").gte(from).lte(to);
			} else if (from != null) {
				criteria.and("createdAt").gte(from);
			} else if (to != null) {
				criteria.and("createdAt").lte(to);
			}
			
			Query query = new Query(criteria).with(pageable);
			
			List <StaffProfile> results = mongoTemplate.find(query, StaffProfile.class);
			long total = mongoTemplate.count( Query.of(query).limit(-1).skip(-1), StaffProfile.class);
			
			return new PageImpl <> (results, pageable, total);
		}
}