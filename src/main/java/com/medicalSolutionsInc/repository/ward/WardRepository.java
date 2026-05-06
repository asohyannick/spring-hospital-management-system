package com.medicalSolutionsInc.repository.ward;

import com.medicalSolutionsInc.entity.ward.Ward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends MongoRepository<Ward, String> {
	
		List<Ward> findByDeletedAtIsNull();
		
		Optional<Ward> findByIdAndDeletedAtIsNull(String id);
		
}