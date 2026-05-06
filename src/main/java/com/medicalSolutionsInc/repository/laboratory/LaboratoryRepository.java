package com.medicalSolutionsInc.repository.laboratory;

import com.medicalSolutionsInc.entity.laboratory.Laboratory;
import com.medicalSolutionsInc.enumerations.laboratoryStatus.LaboratoryStatus;
import com.medicalSolutionsInc.enumerations.laboratoryType.LaboratoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaboratoryRepository extends MongoRepository<Laboratory, String> {

	// Existence checks
	boolean existsByEmail(String email);
	boolean existsByRegistrationNumber(String registrationNumber);
	
	Page <Laboratory> findByDeletedAtIsNull( Pageable pageable);
	Optional<Laboratory> findByIdAndDeletedAtIsNull(String id);
	long countByDeletedAtIsNull();
	// Search
	Page<Laboratory> findByDeletedAtIsNullAndNameContainingIgnoreCaseOrDeletedAtIsNullAndEmailContainingIgnoreCaseOrDeletedAtIsNullAndRegistrationNumberContainingIgnoreCase(
			String name, String email, String registrationNumber, Pageable pageable
	);
}