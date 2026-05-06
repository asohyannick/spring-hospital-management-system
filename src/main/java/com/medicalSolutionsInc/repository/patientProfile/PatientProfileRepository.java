package com.medicalSolutionsInc.repository.patientProfile;

import com.medicalSolutionsInc.entity.patientProfile.PatientProfile;
import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends MongoRepository<PatientProfile, String> {

		boolean existsByEmail(String email);
		boolean existsByNationalId(String nationalId);
		boolean existsByPatientNumber(String patientNumber);
		
		Page<PatientProfile> findByDeletedAtIsNull(Pageable pageable);
		Optional<PatientProfile> findByIdAndDeletedAtIsNull(String id);
		long countByDeletedAtIsNull();
		
		List<PatientProfile> findByPrimaryDoctorIdAndDeletedAtIsNull(String doctorId);
		Page<PatientProfile> findByPrimaryDoctorIdAndDeletedAtIsNull(String doctorId, Pageable pageable);
		Page<PatientProfile> findByGenderAndDeletedAtIsNull(GenderType gender, Pageable pageable);
		Page<PatientProfile> findByBloodGroupAndDeletedAtIsNull(BloodGroup bloodGroup, Pageable pageable);
		Page<PatientProfile> findByActiveAndDeletedAtIsNull(boolean active, Pageable pageable);
		Page<PatientProfile> findByDeceasedAndDeletedAtIsNull(boolean deceased, Pageable pageable);
		
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
		                  { "patient_number":{ "$regex": "?0", "$options": "i" } },
		                  { "national_id":   { "$regex": "?0", "$options": "i" } }
		                ]},
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?1" }, "missing"] } }, { "gender": "?1" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?2" }, "missing"] } }, { "blood_group": "?2" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?3" }, "missing"] } }, { "marital_status": "?3" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?4" }, "missing"] } }, { "primary_doctor_id": "?4" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?5" }, "missing"] } }, { "active": ?5 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?6" }, "missing"] } }, { "deceased": ?6 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?7" }, "missing"] } }, { "created_at": { "$gte": "?7" } }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?8" }, "missing"] } }, { "created_at": { "$lte": "?8" } }] }
		              ]
		            }
		            """)
		Page<PatientProfile> search(
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
		);
}