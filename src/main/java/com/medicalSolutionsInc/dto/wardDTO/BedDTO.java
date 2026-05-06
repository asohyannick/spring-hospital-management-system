package com.medicalSolutionsInc.dto.wardDTO;

import com.medicalSolutionsInc.entity.ward.Ward.Bed.BedStatus;
import java.time.Instant;

public record BedDTO(
		String bedNumber,
		BedStatus status,
		String patientId,
		String patientName,
		Instant admittedAt,
		Instant expectedDischargeAt,
		boolean hasMonitor,
		boolean hasVentilator,
		boolean hasOxygen
) {}