package com.medicalSolutionsInc.mappers.pharmacyMapper;

import com.medicalSolutionsInc.dto.pharmacyDTO.AddressDTO;
import com.medicalSolutionsInc.dto.pharmacyDTO.CreatePharmacyRequestDTO;
import com.medicalSolutionsInc.dto.pharmacyDTO.CreatePharmacyResponseDTO;
import com.medicalSolutionsInc.entity.pharmacy.Pharmacy;
import org.mapstruct.*;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PharmacyMapper {

		@Mapping(target = "id",                      ignore = true)
		@Mapping(target = "pharmacyNumber",          ignore = true)
		@Mapping(target = "status",                  ignore = true)
		@Mapping(target = "verified",                ignore = true)
		@Mapping(target = "totalMedicationsInStock", ignore = true)
		@Mapping(target = "createdAt",               ignore = true)
		@Mapping(target = "updatedAt",               ignore = true)
		@Mapping(target = "deletedAt",               ignore = true)
		Pharmacy toEntity(CreatePharmacyRequestDTO dto);
		
		CreatePharmacyResponseDTO toResponseDTO(Pharmacy pharmacy);
		
		Pharmacy.Address toAddress(AddressDTO dto);
		
		AddressDTO toAddressDTO( Pharmacy.Address address);
		
		@Mapping(target = "id",                      ignore = true)
		@Mapping(target = "pharmacyNumber",          ignore = true)
		@Mapping(target = "status",                  ignore = true)
		@Mapping(target = "verified",                ignore = true)
		@Mapping(target = "totalMedicationsInStock", ignore = true)
		@Mapping(target = "createdAt",               ignore = true)
		@Mapping(target = "updatedAt",               ignore = true)
		@Mapping(target = "deletedAt",               ignore = true)
		void updateEntityFromDTO(CreatePharmacyRequestDTO dto, @MappingTarget Pharmacy pharmacy);
}