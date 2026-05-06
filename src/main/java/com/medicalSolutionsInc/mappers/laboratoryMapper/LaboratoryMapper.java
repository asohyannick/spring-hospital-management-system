package com.medicalSolutionsInc.mappers.laboratoryMapper;

import com.medicalSolutionsInc.dto.laboratoryDTO.LaboratoryRequestDTO;
import com.medicalSolutionsInc.dto.laboratoryDTO.LaboratoryResponseDTO;
import com.medicalSolutionsInc.entity.laboratory.Laboratory;
import org.mapstruct.*;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LaboratoryMapper {

		@Mappings({
				@Mapping(target = "id",        ignore = true),
				@Mapping(target = "verified",  ignore = true),
				@Mapping(target = "createdAt", ignore = true),
				@Mapping(target = "updatedAt", ignore = true),
				@Mapping(target = "deletedAt", ignore = true),
				@Mapping(target = "address",   source = "address")
		})
		Laboratory toEntity(LaboratoryRequestDTO dto);
		
		@Mapping(target = "address", source = "address")
		LaboratoryResponseDTO toResponseDTO(Laboratory laboratory);
		
		@Mappings({
				@Mapping(target = "id",        ignore = true),
				@Mapping(target = "verified",  ignore = true),
				@Mapping(target = "createdAt", ignore = true),
				@Mapping(target = "updatedAt", ignore = true),
				@Mapping(target = "deletedAt", ignore = true),
		})
		void updateEntityFromDTO(LaboratoryRequestDTO dto, @MappingTarget Laboratory laboratory);


}