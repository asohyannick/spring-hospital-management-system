package com.medicalSolutionsInc.dto.wardDTO;

import com.medicalSolutionsInc.enumerations.genderRestriction.GenderRestriction;
import com.medicalSolutionsInc.enumerations.wardStatus.WardStatus;
import com.medicalSolutionsInc.enumerations.wardType.WardType;

import java.time.Instant;
import java.util.List;

public record CreateWardResponseDTO(
		
		String id,
		String wardNumber,
		String name,
		WardType type,
		WardStatus status,
		String description,
		String floor,
		String building,
		String facilityId,
		String facilityName,
		int totalBeds,
		int availableBeds,
		int occupiedBeds,
		int reservedBeds,
		List<BedDTO> beds,
		String wardInChargeId,
		String wardInChargeName,
		List<String> assignedDoctorIds,
		List<String> assignedNurseIds,
		int staffCount,
		List<String> equipment,
		List<String> amenities,
		GenderRestriction genderRestriction,
		VisitingHoursDTO visitingHours,
		boolean isolationWard,
		boolean privateWard,
		boolean pediatric,
		boolean acceptsEmergency,
		Instant createdAt,
		Instant updatedAt
) {}