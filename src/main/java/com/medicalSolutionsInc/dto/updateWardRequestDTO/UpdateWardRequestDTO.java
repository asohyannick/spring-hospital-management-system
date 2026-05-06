package com.medicalSolutionsInc.dto.updateWardRequestDTO;
import com.medicalSolutionsInc.dto.wardDTO.BedDTO;
import com.medicalSolutionsInc.dto.wardDTO.VisitingHoursDTO;
import com.medicalSolutionsInc.enumerations.genderRestriction.GenderRestriction;
import com.medicalSolutionsInc.enumerations.wardStatus.WardStatus;
import com.medicalSolutionsInc.enumerations.wardType.WardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

@Schema(description = "Request payload for updating an existing ward (all fields optional)")
public record UpdateWardRequestDTO(
		
		@Schema(description = "Updated ward name")
		String name,
		
		@Schema(description = "Updated ward type")
		WardType type,
		
		@Schema(description = "Updated ward status")
		WardStatus status,
		
		@Schema(description = "Updated description")
		String description,
		
		@Schema(description = "Updated floor")
		String floor,
		
		@Schema(description = "Updated building")
		String building,
		
		@Schema(description = "Updated facility ID")
		String facilityId,
		
		@Schema(description = "Updated facility name")
		String facilityName,
		
		@Min(value = 1, message = "Total beds must be at least 1")
		@Schema(description = "Updated total bed count")
		Integer totalBeds,
		
		@Valid
		@Schema(description = "Updated bed list")
		List< BedDTO > beds,
		
		@Schema(description = "Updated ward in-charge ID")
		String wardInChargeId,
		
		@Schema(description = "Updated ward in-charge name")
		String wardInChargeName,
		
		@Schema(description = "Updated assigned doctor IDs")
		List<String> assignedDoctorIds,
		
		@Schema(description = "Updated assigned nurse IDs")
		List<String> assignedNurseIds,
		
		@Schema(description = "Updated equipment list")
		List<String> equipment,
		
		@Schema(description = "Updated amenities list")
		List<String> amenities,
		
		@Schema(description = "Updated gender restriction")
		GenderRestriction genderRestriction,
		
		@Valid
		@Schema(description = "Updated visiting hours")
		VisitingHoursDTO visitingHours,
		
		@Schema(description = "Updated isolation ward flag")
		Boolean isolationWard,
		
		@Schema(description = "Updated private ward flag")
		Boolean privateWard,
		
		@Schema(description = "Updated pediatric flag")
		Boolean pediatric,
		
		@Schema(description = "Updated emergency acceptance flag")
		Boolean acceptsEmergency
) {}