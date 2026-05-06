package com.medicalSolutionsInc.mappers.medicalRecordMapper;

import com.medicalSolutionsInc.dto.medicalRecordDTO.MedicalRecordRequestDTO;
import com.medicalSolutionsInc.dto.medicalRecordDTO.MedicalRecordResponseDTO;
import com.medicalSolutionsInc.entity.medicalRecord.MedicalRecord;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import org.mapstruct.*;


@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MedicalRecordMapper {
		
		@Mappings({
				@Mapping(target = "id",            ignore = true),
				@Mapping(target = "recordNumber",  ignore = true),
				@Mapping(target = "archived",      ignore = true),
				@Mapping(target = "createdAt",     ignore = true),
				@Mapping(target = "updatedAt",     ignore = true),
				@Mapping(target = "deletedAt",     ignore = true),
				@Mapping(target = "attachments",   ignore = true),
				@Mapping(target = "prescriptions", source = "prescriptions"),
				@Mapping(target = "labResults",    source = "labResults"),
				@Mapping(target = "vitalSigns",    source = "vitalSigns")
		})
		MedicalRecord toEntity(MedicalRecordRequestDTO dto);
		
		@Mappings({
				@Mapping(target = "prescriptions", source = "prescriptions"),
				@Mapping(target = "labResults",    source = "labResults"),
				@Mapping(target = "vitalSigns",    source = "vitalSigns"),
				@Mapping(target = "attachments",   source = "attachments")
		})
		MedicalRecordResponseDTO toResponseDTO(MedicalRecord medicalRecord);
		
		@Mappings({
				@Mapping(target = "id",           ignore = true),
				@Mapping(target = "recordNumber", ignore = true),
				@Mapping(target = "archived",     ignore = true),
				@Mapping(target = "createdAt",    ignore = true),
				@Mapping(target = "updatedAt",    ignore = true),
				@Mapping(target = "deletedAt",    ignore = true),
				@Mapping(target = "attachments",  ignore = true),
		})
		void updateEntityFromDTO(MedicalRecordRequestDTO dto, @MappingTarget MedicalRecord medicalRecord);
		
		default GenderType map( String value) throws Exception {
			if (value == null || value.isBlank()) return null;
			return GenderType.valueOf(value.trim().toUpperCase());
		}
}