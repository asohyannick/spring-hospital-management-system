package com.medicalSolutionsInc.repository.laboratory;

import com.medicalSolutionsInc.entity.laboratory.Laboratory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LaboratoryRepository extends MongoRepository<Laboratory, String> {

	boolean existsByEmail(String email);
	boolean existsByRegistrationNumber(String registrationNumber);
	
	Page <Laboratory> findByDeletedAtIsNull( Pageable pageable);
	Optional<Laboratory> findByIdAndDeletedAtIsNull(String id);
	long countByDeletedAtIsNull();
	@Query ("{ 'deletedAt': null, $or: [ " +
			       "{ 'name': { $regex: ?0, $options: 'i' } }, " +
			       "{ 'email': { $regex: ?0, $options: 'i' } }, " +
			       "{ 'registrationNumber': { $regex: ?0, $options: 'i' } } " +
			       "] }")
	Page<Laboratory> searchByKeyword(String keyword, Pageable pageable);
}