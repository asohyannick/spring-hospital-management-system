package com.medicalSolutionsInc.repository.medicalRecord;
import com.medicalSolutionsInc.entity.medicalRecord.MedicalRecord;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import com.medicalSolutionsInc.enumerations.visitType.VisitType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {

		boolean existsByRecordNumber(String recordNumber);
		boolean existsByPatientIdAndType(String patientId, MedicalRecordType type);
		
		Optional<MedicalRecord> findByRecordNumber(String recordNumber);
		
		@Query("{ 'deleted_at': null }")
		List<MedicalRecord> findAllActive();
		
		@Query("{ '_id': ?0, 'deleted_at': null }")
		Optional<MedicalRecord> findActiveById(String id);
		
		@Query("{ 'record_number': ?0, 'deleted_at': null }")
		Optional<MedicalRecord> findActiveByRecordNumber(String recordNumber);
		
		@Query("{ 'patient_id': ?0, 'deleted_at': null }")
		List<MedicalRecord> findAllActiveByPatientId(String patientId);
		
		@Query("{ 'patient_id': ?0, 'type': ?1, 'deleted_at': null }")
		List<MedicalRecord> findByPatientIdAndType(String patientId, MedicalRecordType type);
		
		@Query("{ 'patient_id': ?0, 'status': ?1, 'deleted_at': null }")
		List<MedicalRecord> findByPatientIdAndStatus(String patientId, MedicalRecordStatus status);
		
		@Query("{ 'attending_doctor_id': ?0, 'deleted_at': null }")
		List<MedicalRecord> findAllActiveByDoctorId(String doctorId);
		
		@Query("{ 'attending_doctor_id': ?0, 'status': ?1, 'deleted_at': null }")
		List<MedicalRecord> findByDoctorIdAndStatus(String doctorId, MedicalRecordStatus status);
		
		List<MedicalRecord> findByType(MedicalRecordType type);
		List<MedicalRecord> findByStatus(MedicalRecordStatus status);
		List<MedicalRecord> findByTypeAndStatus(MedicalRecordType type, MedicalRecordStatus status);
		
		List<MedicalRecord> findByVisitType(VisitType visitType);
		
		@Query("{ 'facility_id': ?0, 'deleted_at': null }")
		List<MedicalRecord> findAllActiveByFacilityId(String facilityId);
		
		@Query("{ 'department': ?0, 'deleted_at': null }")
		List<MedicalRecord> findAllActiveByDepartment(String department);
		
		@Query("{ 'check_in': { $gte: ?0, $lte: ?1 }, 'deleted_at': null }")
		List<MedicalRecord> findByCheckInBetween(Instant from, Instant to);
		
		@Query("{ 'created_at': { $gte: ?0, $lte: ?1 }, 'deleted_at': null }")
		List<MedicalRecord> findByCreatedAtBetween(Instant from, Instant to);
		
		@Query("{ 'is_confidential': true, 'deleted_at': null }")
		List<MedicalRecord> findAllConfidential();
		
		@Query("{ 'is_archived': true }")
		List<MedicalRecord> findAllArchived();
		
		long countByStatus(MedicalRecordStatus status);
		long countByType(MedicalRecordType type);
		long countByPatientId(String patientId);
		
		@Query(value = "{ 'deleted_at': null }", count = true)
		long countAllActive();
}