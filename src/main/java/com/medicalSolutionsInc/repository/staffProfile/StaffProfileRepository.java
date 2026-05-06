package com.medicalSolutionsInc.repository.staffProfile;

import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffProfileRepository extends MongoRepository<StaffProfile, String> {

		Optional<StaffProfile> findByEmail(String email);
		
		Optional<StaffProfile> findByStaffNumber(String staffNumber);
		
		Optional<StaffProfile> findByLicenseNumber(String licenseNumber);
		
		boolean existsByEmail(String email);
		
		boolean existsByStaffNumber(String staffNumber);
		
		boolean existsByLicenseNumber(String licenseNumber);
		
		List<StaffProfile> findByFacilityId(String facilityId);
		
		List<StaffProfile> findByRole(StaffRole role);
		
		List<StaffProfile> findByDepartment(String department);
		
		List<StaffProfile> findByEmploymentStatus(EmploymentStatus employmentStatus);
		
		List<StaffProfile> findByFacilityIdAndRole(String facilityId, StaffRole role);
		
		List<StaffProfile> findByFacilityIdAndEmploymentStatus(String facilityId, EmploymentStatus status);
		
		List<StaffProfile> findByDeletedAtIsNull();
		
		Optional<StaffProfile> findByIdAndDeletedAtIsNull(String id);
}