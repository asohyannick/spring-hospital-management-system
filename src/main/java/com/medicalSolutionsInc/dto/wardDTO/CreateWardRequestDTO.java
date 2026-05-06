package com.medicalSolutionsInc.dto.wardDTO;

import com.medicalSolutionsInc.enumerations.genderRestriction.GenderRestriction;
import com.medicalSolutionsInc.enumerations.wardStatus.WardStatus;
import com.medicalSolutionsInc.enumerations.wardType.WardType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateWardRequestDTO(
		
		@NotBlank(message = "Ward name is required")
		String name,
		
		@NotNull(message = "Ward type is required")
		WardType type,
		
		WardStatus status,
		
		String description,
		
		String floor,
		
		String building,
		
		@NotBlank(message = "Facility ID is required")
		String facilityId,
		
		String facilityName,
		
		@NotNull(message = "Total beds is required")
		@Min(value = 1, message = "Ward must have at least one bed")
		int totalBeds,
		
		@Valid
		List<BedDTO> beds,
		
		String wardInChargeId,
		String wardInChargeName,
		
		List<String> assignedDoctorIds,
		List<String> assignedNurseIds,
		
		List<String> equipment,
		List<String> amenities,
		
		GenderRestriction genderRestriction,
		
		@Valid
		VisitingHoursDTO visitingHours,
		
		boolean isolationWard,
		boolean privateWard,
		boolean pediatric,
		boolean acceptsEmergency
) {}