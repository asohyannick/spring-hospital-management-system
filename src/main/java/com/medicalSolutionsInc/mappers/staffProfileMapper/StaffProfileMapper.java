package com.medicalSolutionsInc.mappers.staffProfileMapper;

import com.medicalSolutionsInc.dto.staffProfileDTO.*;
import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StaffProfileMapper {

		@Mapping(target = "id", ignore = true)
		@Mapping(target = "staffNumber", ignore = true)
		@Mapping(target = "verified", ignore = true)
		@Mapping(target = "createdAt", ignore = true)
		@Mapping(target = "updatedAt", ignore = true)
		@Mapping(target = "deletedAt", ignore = true)
		@Mapping(target = "terminationDate", ignore = true)
		StaffProfile toEntity(CreateStaffProfileRequestDTO dto);
		
		CreateStaffProfileResponseDTO toResponseDTO(StaffProfile entity);
		
		StaffProfile.Address toAddress(AddressDTO dto);
		AddressDTO toAddressDTO(StaffProfile.Address address);
		
		StaffProfile.Certification toCertification(CertificationDTO dto);
		CertificationDTO toCertificationDTO(StaffProfile.Certification certification);
		
		StaffProfile.Education toEducation(EducationDTO dto);
		EducationDTO toEducationDTO(StaffProfile.Education education);
		
		StaffProfile.EmergencyContact toEmergencyContact(EmergencyContactDTO dto);
		EmergencyContactDTO toEmergencyContactDTO(StaffProfile.EmergencyContact contact);
}