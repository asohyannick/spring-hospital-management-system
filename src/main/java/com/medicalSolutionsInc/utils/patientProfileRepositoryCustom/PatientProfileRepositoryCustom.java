package com.medicalSolutionsInc.utils.patientProfileRepositoryCustom;

import com.medicalSolutionsInc.entity.patientProfile.PatientProfile;
import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface PatientProfileRepositoryCustom {
Page < PatientProfile > search(
		String keyword,
		GenderType gender,
		BloodGroup bloodGroup,
		MaritalStatus maritalStatus,
		String primaryDoctorId,
		Boolean active,
		Boolean deceased,
		Instant from,
		Instant to,
		Pageable pageable
);
}