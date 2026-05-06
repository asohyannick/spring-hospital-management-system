package com.medicalSolutionsInc.repository.medicalRecord;
import com.medicalSolutionsInc.entity.medicalRecord.MedicalRecord;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {
		
		Page <MedicalRecord> findByDeletedAtIsNull( Pageable pageable);
		Optional<MedicalRecord> findByIdAndDeletedAtIsNull(String id);
		long countByDeletedAtIsNull();
		
		Page<MedicalRecord> findByPatientIdAndDeletedAtIsNull(String patientId, Pageable pageable);
		Page<MedicalRecord> findByAttendingDoctorIdAndDeletedAtIsNull(String doctorId, Pageable pageable);
		Page<MedicalRecord> findByFacilityIdAndDeletedAtIsNull(String facilityId, Pageable pageable);
		
		Page<MedicalRecord> findByStatusAndDeletedAtIsNull(MedicalRecordStatus status, Pageable pageable);
		Page<MedicalRecord> findByTypeAndDeletedAtIsNull(MedicalRecordType type, Pageable pageable);
		List<MedicalRecord> findByPatientIdAndDeletedAtIsNull(String patientId);
		
		@Query("""
		            {
		              "deleted_at": null,
		              "$and": [
		                { "$or": [
		                  { "$expr": { "$eq": [{ "$type": "?0" }, "missing"] } },
		                  { "patient_name":    { "$regex": "?0", "$options": "i" } },
		                  { "title":           { "$regex": "?0", "$options": "i" } },
		                  { "chief_complaint": { "$regex": "?0", "$options": "i" } },
		                  { "record_number":   { "$regex": "?0", "$options": "i" } }
		                ]},
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?1" }, "missing"] } }, { "status": "?1" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?2" }, "missing"] } }, { "type": "?2" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?3" }, "missing"] } }, { "patient_id": "?3" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?4" }, "missing"] } }, { "attending_doctor_id": "?4" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?5" }, "missing"] } }, { "facility_id": "?5" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?6" }, "missing"] } }, { "created_at": { "$gte": "?6" } }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?7" }, "missing"] } }, { "created_at": { "$lte": "?7" } }] }
		              ]
		            }
		            """)
		Page<MedicalRecord> search(
				String keyword,
				MedicalRecordStatus status,
				MedicalRecordType type,
				String patientId,
				String attendingDoctorId,
				String facilityId,
				Instant from,
				Instant to,
				Pageable pageable
		);
}