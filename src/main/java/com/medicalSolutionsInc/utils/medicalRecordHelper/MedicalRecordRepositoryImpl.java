package com.medicalSolutionsInc.utils.medicalRecordHelper;

import com.medicalSolutionsInc.entity.medicalRecord.MedicalRecord;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
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
public class MedicalRecordRepositoryImpl implements MedicalRecordRepositoryCustom {

		private final MongoTemplate mongoTemplate;
		
		@Override
		public Page < MedicalRecord > search(
				String keyword,
				MedicalRecordStatus status,
				MedicalRecordType type,
				String patientId,
				String attendingDoctorId,
				String facilityId,
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
								Criteria.where("patientName").regex(pattern),
								Criteria.where("chiefComplaint").regex(pattern),
								Criteria.where("recordNumber").regex(pattern)
						)
				);
			}
			
			if (status != null)            criteria.and("status").is(status);
			if (type != null)              criteria.and("type").is(type);
			if (patientId != null)         criteria.and("patientId").is(patientId);
			if (attendingDoctorId != null) criteria.and("attendingDoctorId").is(attendingDoctorId);
			if (facilityId != null)        criteria.and("facilityId").is(facilityId);
			
			if (from != null && to != null) {
				criteria.and("createdAt").gte(from).lte(to);
			} else if (from != null) {
				criteria.and("createdAt").gte(from);
			} else if (to != null) {
				criteria.and("createdAt").lte(to);
			}
			
			Query query = new Query(criteria).with(pageable);
			
			List <MedicalRecord> results = mongoTemplate.find(query, MedicalRecord.class);
			long total = mongoTemplate.count( Query.of(query).limit(-1).skip(-1), MedicalRecord.class);
			
			return new PageImpl <> (results, pageable, total);
		}
}