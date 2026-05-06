package com.medicalSolutionsInc.dto.pharmacyDTO;

import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;

import java.time.Instant;
import java.util.List;

public record CreatePharmacyResponseDTO(
		
		String id,
		
		String pharmacyNumber,
		
		String name,
		
		String licenseNumber,
		
		PharmacyType type,
		
		PharmacyStatus status,
		
		String description,
		
		String imageUrl,
		
		String email,
		
		String phone,
		
		String alternatePhone,
		
		String fax,
		
		String website,
		
		AddressDTO address,
		
		String operatingHours,
		
		List<String> operatingDays,
		
		List<String> servicesOffered,
		
		List<String> acceptedInsuranceProviders,
		
		List<String> acceptedPaymentMethods,
		
		String headPharmacist,
		
		String contactPerson,
		
		int staffCount,
		
		int totalMedicationsInStock,
		
		int lowStockThreshold,
		
		boolean verified,
		
		boolean open24Hours,
		
		boolean acceptsWalkIns,
		
		boolean acceptsOnlineOrders,
		
		boolean offersDelivery,
		
		double deliveryRadiusKm,
		
		Instant createdAt,
		
		Instant updatedAt
) { }