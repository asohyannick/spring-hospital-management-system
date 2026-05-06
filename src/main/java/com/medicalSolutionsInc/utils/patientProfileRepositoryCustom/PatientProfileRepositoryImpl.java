package com.medicalSolutionsInc.utils.patientProfileRepositoryCustom;

import com.medicalSolutionsInc.entity.patientProfile.PatientProfile;
import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
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
public class PatientProfileRepositoryImpl implements PatientProfileRepositoryCustom {
		
		private final MongoTemplate mongoTemplate;
		
		@Override
		public Page < PatientProfile > search(
				String keyword,
				GenderType gender,
				BloodGroup bloodGroup,
				MaritalStatus maritalStatus,
				String primaryDoctorId,
				Boolean active,
				Boolean deceased,
				Instant from,
				Instant to,
				Pageable pageable
		) {
			Criteria criteria = Criteria.where("deletedAt").isNull();
			
			// Keyword — matches firstName, lastName, email, phoneNumber, patientNumber, nationalId
			if ( StringUtils.hasText(keyword)) {
				Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
				criteria.andOperator(
						new Criteria().orOperator(
								Criteria.where("firstName").regex(pattern),
								Criteria.where("lastName").regex(pattern),
								Criteria.where("email").regex(pattern),
								Criteria.where("phoneNumber").regex(pattern),
								Criteria.where("patientNumber").regex(pattern),
								Criteria.where("nationalId").regex(pattern)
						)
				);
			}
			
			if (gender != null)          criteria.and("gender").is(gender);
			if (bloodGroup != null)      criteria.and("bloodGroup").is(bloodGroup);
			if (maritalStatus != null)   criteria.and("maritalStatus").is(maritalStatus);
			if (primaryDoctorId != null) criteria.and("primaryDoctorId").is(primaryDoctorId);
			if (active != null)          criteria.and("active").is(active);
			if (deceased != null)        criteria.and("deceased").is(deceased);
			
			if (from != null && to != null) {
				criteria.and("createdAt").gte(from).lte(to);
			} else if (from != null) {
				criteria.and("createdAt").gte(from);
			} else if (to != null) {
				criteria.and("createdAt").lte(to);
			}
			
			Query query = new Query(criteria).with(pageable);
			
			List <PatientProfile> results = mongoTemplate.find(query, PatientProfile.class);
			long total = mongoTemplate.count( Query.of(query).limit(-1).skip(-1), PatientProfile.class);
			
			return new PageImpl <> (results, pageable, total);
		}
}