package com.medicalSolutionsInc.repository.patientProfile;

import com.medicalSolutionsInc.entity.patientProfile.PatientProfile;
import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends MongoRepository<PatientProfile, String> {

		boolean existsByEmail(String email);
		boolean existsByPatientNumber(String patientNumber);
		boolean existsByNationalId(String nationalId);
		boolean existsByPhoneNumber(String phoneNumber);
		
		Optional<PatientProfile> findByEmail(String email);
		Optional<PatientProfile> findByPatientNumber(String patientNumber);
		Optional<PatientProfile> findByNationalId(String nationalId);
		
		@Query("{ 'deleted_at': null }")
		List<PatientProfile> findAllActive();
		
		@Query("{ '_id': ?0, 'deleted_at': null }")
		Optional<PatientProfile> findActiveById(String id);
		
		@Query("{ 'email': ?0, 'deleted_at': null }")
		Optional<PatientProfile> findActiveByEmail(String email);
		
		@Query("{ 'patient_number': ?0, 'deleted_at': null }")
		Optional<PatientProfile> findActiveByPatientNumber(String patientNumber);
		
		@Query("{ 'primary_doctor_id': ?0, 'deleted_at': null }")
		List<PatientProfile> findAllActiveByDoctorId(String doctorId);
		
		List<PatientProfile> findByGender(GenderType gender);
		List<PatientProfile> findByBloodGroup(BloodGroup bloodGroup);
		List<PatientProfile> findByMaritalStatus(MaritalStatus maritalStatus);
		List<PatientProfile> findByNationality(String nationality);
		
		@Query("{ 'address.city': ?0, 'deleted_at': null }")
		List<PatientProfile> findActiveByCity(String city);
		
		@Query("{ 'address.state': ?0, 'deleted_at': null }")
		List<PatientProfile> findActiveByState(String state);
		
		@Query("{ 'address.country': ?0, 'deleted_at': null }")
		List<PatientProfile> findActiveByCountry(String country);
		
		@Query("{ 'first_name': { $regex: ?0, $options: 'i' }, 'deleted_at': null }")
		List<PatientProfile> searchByFirstName(String firstName);
		
		@Query("{ 'last_name': { $regex: ?0, $options: 'i' }, 'deleted_at': null }")
		List<PatientProfile> searchByLastName(String lastName);
		
		@Query("{ $or: [ { 'first_name': { $regex: ?0, $options: 'i' } }, { 'last_name': { $regex: ?0, $options: 'i' } } ], 'deleted_at': null }")
		List<PatientProfile> searchByName(String name);
		
		@Query("{ 'is_active': true, 'deleted_at': null }")
		List<PatientProfile> findAllActivePatients();
		
		@Query("{ 'is_deceased': true }")
		List<PatientProfile> findAllDeceased();
		
		@Query("{ 'insurance.active': true, 'deleted_at': null }")
		List<PatientProfile> findAllWithActiveInsurance();
		
		long countByGender(GenderType gender);
		long countByBloodGroup(BloodGroup bloodGroup);
		long countByNationality(String nationality);
		
		@Query(value = "{ 'deleted_at': null }", count = true)
		long countAllActive();
		
		@Query(value = "{ 'is_deceased': true }", count = true)
		long countAllDeceased();
}