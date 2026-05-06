package com.medicalSolutionsInc.repository.staffProfile;

import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface StaffProfileRepository extends MongoRepository<StaffProfile, String> {
		
		boolean existsByEmail(String email);
		boolean existsByLicenseNumber(String licenseNumber);
		
		Page<StaffProfile> findByDeletedAtIsNull(Pageable pageable);
		Optional<StaffProfile> findByIdAndDeletedAtIsNull(String id);
		long countByDeletedAtIsNull();
		
		@Query("""
		            {
		              "deleted_at": null,
		              "$and": [
		                { "$or": [
		                  { "$expr": { "$eq": [{ "$type": "?0" }, "missing"] } },
		                  { "first_name":    { "$regex": "?0", "$options": "i" } },
		                  { "last_name":     { "$regex": "?0", "$options": "i" } },
		                  { "email":         { "$regex": "?0", "$options": "i" } },
		                  { "phone_number":  { "$regex": "?0", "$options": "i" } },
		                  { "staff_number":  { "$regex": "?0", "$options": "i" } },
		                  { "license_number":{ "$regex": "?0", "$options": "i" } },
		                  { "department":    { "$regex": "?0", "$options": "i" } },
		                  { "specialization":{ "$regex": "?0", "$options": "i" } }
		                ]},
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?1" }, "missing"] } }, { "role": "?1" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?2" }, "missing"] } }, { "gender": "?2" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?3" }, "missing"] } }, { "employment_status": "?3" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?4" }, "missing"] } }, { "department": "?4" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?5" }, "missing"] } }, { "facility_id": "?5" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?6" }, "missing"] } }, { "is_verified": ?6 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?7" }, "missing"] } }, { "is_available": ?7 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?8" }, "missing"] } }, { "created_at": { "$gte": "?8" } }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?9" }, "missing"] } }, { "created_at": { "$lte": "?9" } }] }
		              ]
		            }
		            """)
		Page<StaffProfile> search(
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
		);
}