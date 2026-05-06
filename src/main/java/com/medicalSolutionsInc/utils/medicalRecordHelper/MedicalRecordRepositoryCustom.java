package com.medicalSolutionsInc.utils.medicalRecordHelper;

import com.medicalSolutionsInc.entity.medicalRecord.MedicalRecord;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface MedicalRecordRepositoryCustom {
Page < MedicalRecord > search(
		String keyword,
		MedicalRecordStatus status,
		MedicalRecordType type,
		String patientId,
		String attendingDoctorId,
		String facilityId,
		Instant from,
		Instant to,
		Pageable pageable
);
}