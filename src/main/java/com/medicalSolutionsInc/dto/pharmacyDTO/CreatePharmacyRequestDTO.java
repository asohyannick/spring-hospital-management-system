package com.medicalSolutionsInc.dto.pharmacyDTO;

import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePharmacyRequestDTO(
		
		@NotBlank(message = "Pharmacy name is required")
		String name,
		
		@NotBlank(message = "License number is required")
		String licenseNumber,
		
		@NotNull(message = "Pharmacy type is required")
		PharmacyType type,
		
		String description,
		
		String imageUrl,
		
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		String email,
		
		@NotBlank(message = "Phone is required")
		String phone,
		
		String alternatePhone,
		
		String fax,
		
		String website,
		
		@NotNull(message = "Address is required")
		AddressDTO address,
		
		String operatingHours,
		
		List<String> operatingDays,
		
		List<String> servicesOffered,
		
		List<String> acceptedInsuranceProviders,
		
		List<String> acceptedPaymentMethods,
		
		String headPharmacist,
		
		String contactPerson,
		
		int staffCount,
		
		int lowStockThreshold,
		
		boolean open24Hours,
		
		boolean acceptsWalkIns,
		
		boolean acceptsOnlineOrders,
		
		boolean offersDelivery,
		
		double deliveryRadiusKm
) {
public record AddressDTO(
		@NotBlank(message = "Street is required")
		String street,
		
		@NotBlank(message = "City is required")
		String city,
		
		String state,
		
		@NotBlank(message = "Country is required")
		String country,
		
		String zipCode,
		
		double latitude,
		
		double longitude
) {}
}