package com.medicalSolutionsInc.utils.staffProfileRepositoryCustom;

import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface StaffProfileRepositoryCustom {
	Page < StaffProfile > search(
			String keyword,
			StaffRole role,
			GenderType gender,
			EmploymentStatus employmentStatus,
			String department,
			String facilityId,
			Boolean verified,
			Boolean available,
			Instant from,
			Instant to,
			Pageable pageable
	);
}