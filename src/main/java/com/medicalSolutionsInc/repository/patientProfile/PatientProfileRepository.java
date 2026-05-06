package com.medicalSolutionsInc.repository.patientProfile;

import com.medicalSolutionsInc.entity.patientProfile.PatientProfile;
import com.medicalSolutionsInc.utils.patientProfileRepositoryCustom.PatientProfileRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends MongoRepository<PatientProfile, String>, PatientProfileRepositoryCustom {

		boolean existsByEmail(String email);
		boolean existsByNationalId(String nationalId);
		
		Page<PatientProfile> findByDeletedAtIsNull(Pageable pageable);
		Optional<PatientProfile> findByIdAndDeletedAtIsNull(String id);
		long countByDeletedAtIsNull();
		
}