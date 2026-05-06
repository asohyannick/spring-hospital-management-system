package com.medicalSolutionsInc.repository.staffProfile;

import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import com.medicalSolutionsInc.utils.staffProfileRepositoryCustom.StaffProfileRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffProfileRepository extends MongoRepository<StaffProfile, String>, StaffProfileRepositoryCustom {
		
		boolean existsByEmail(String email);
		boolean existsByLicenseNumber(String licenseNumber);
		
		Page<StaffProfile> findByDeletedAtIsNull(Pageable pageable);
		Optional<StaffProfile> findByIdAndDeletedAtIsNull(String id);
		long countByDeletedAtIsNull();
		
}