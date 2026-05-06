package com.medicalSolutionsInc.mappers.patientProfileMapper;

import com.medicalSolutionsInc.dto.patientProfileDTO.CreatePatientProfileRequestDTO;
import com.medicalSolutionsInc.dto.patientProfileDTO.CreatePatientProfileResponseDTO;
import com.medicalSolutionsInc.entity.patientProfile.PatientProfile;
import org.mapstruct.*;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PatientProfileMapper {

		@Mappings({
				@Mapping(target = "id",            ignore = true),
				@Mapping(target = "patientNumber", ignore = true),
				@Mapping(target = "active",        ignore = true),
				@Mapping(target = "deceased",      ignore = true),
				@Mapping(target = "deceasedAt",    ignore = true),
				@Mapping(target = "createdAt",     ignore = true),
				@Mapping(target = "updatedAt",     ignore = true),
				@Mapping(target = "deletedAt",     ignore = true),
				@Mapping(target = "address",       source = "address"),
				@Mapping(target = "emergencyContact", source = "emergencyContact"),
				@Mapping(target = "insurance",     source = "insurance")
		})
		PatientProfile toEntity(CreatePatientProfileRequestDTO dto);
		
		@Mappings({
				@Mapping(target = "address",          source = "address"),
				@Mapping(target = "emergencyContact",  source = "emergencyContact"),
				@Mapping(target = "insurance",         source = "insurance")
		})
		CreatePatientProfileResponseDTO toResponseDTO(PatientProfile patientProfile);
		
		@Mappings({
				@Mapping(target = "id",            ignore = true),
				@Mapping(target = "patientNumber", ignore = true),
				@Mapping(target = "active",        ignore = true),
				@Mapping(target = "deceased",      ignore = true),
				@Mapping(target = "deceasedAt",    ignore = true),
				@Mapping(target = "createdAt",     ignore = true),
				@Mapping(target = "updatedAt",     ignore = true),
				@Mapping(target = "deletedAt",     ignore = true),
		})
		void updateEntityFromDTO(CreatePatientProfileRequestDTO dto, @MappingTarget PatientProfile patientProfile);

}