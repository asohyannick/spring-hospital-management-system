package com.medicalSolutionsInc.mappers.staffProfileMapper;

import com.medicalSolutionsInc.dto.staffProfileDTO.*;
import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import org.mapstruct.*;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StaffProfileMapper {

		@Mapping(target = "id",               ignore = true)
		@Mapping(target = "staffNumber",      ignore = true)
		@Mapping(target = "verified",         ignore = true)
		@Mapping(target = "createdAt",        ignore = true)
		@Mapping(target = "updatedAt",        ignore = true)
		@Mapping(target = "deletedAt",        ignore = true)
		@Mapping(target = "terminationDate",  ignore = true)
		StaffProfile toEntity(CreateStaffProfileRequestDTO dto);
		
		CreateStaffProfileResponseDTO toResponseDTO(StaffProfile entity);
		
		@Mapping(target = "id",               ignore = true)
		@Mapping(target = "staffNumber",      ignore = true)
		@Mapping(target = "verified",         ignore = true)
		@Mapping(target = "createdAt",        ignore = true)
		@Mapping(target = "updatedAt",        ignore = true)
		@Mapping(target = "deletedAt",        ignore = true)
		@Mapping(target = "terminationDate",  ignore = true)
		void updateEntityFromDTO(CreateStaffProfileRequestDTO dto, @MappingTarget StaffProfile staff);
}