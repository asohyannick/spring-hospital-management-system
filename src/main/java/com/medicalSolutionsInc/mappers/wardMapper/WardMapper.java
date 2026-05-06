package com.medicalSolutionsInc.mappers.wardMapper;

import com.medicalSolutionsInc.dto.wardDTO.BedDTO;
import com.medicalSolutionsInc.dto.wardDTO.CreateWardRequestDTO;
import com.medicalSolutionsInc.dto.wardDTO.CreateWardResponseDTO;
import com.medicalSolutionsInc.dto.wardDTO.VisitingHoursDTO;
import com.medicalSolutionsInc.entity.ward.Ward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WardMapper {

		@Mapping(target = "id", ignore = true)
		@Mapping(target = "wardNumber", ignore = true)
		@Mapping(target = "availableBeds", ignore = true)
		@Mapping(target = "occupiedBeds", ignore = true)
		@Mapping(target = "reservedBeds", ignore = true)
		@Mapping(target = "staffCount", ignore = true)
		@Mapping(target = "createdAt", ignore = true)
		@Mapping(target = "updatedAt", ignore = true)
		@Mapping(target = "deletedAt", ignore = true)
		Ward toEntity(CreateWardRequestDTO dto);
		
		CreateWardResponseDTO toResponseDTO(Ward ward);
		
		Ward.Bed toBed(BedDTO dto);
		BedDTO toBedDTO(Ward.Bed bed);
		
		Ward.VisitingHours toVisitingHours(VisitingHoursDTO dto);
		VisitingHoursDTO toVisitingHoursDTO(Ward.VisitingHours visitingHours);
}