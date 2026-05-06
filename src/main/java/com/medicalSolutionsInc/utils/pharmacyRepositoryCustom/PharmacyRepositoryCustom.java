package com.medicalSolutionsInc.utils.pharmacyRepositoryCustom;

import com.medicalSolutionsInc.entity.pharmacy.Pharmacy;
import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface PharmacyRepositoryCustom {
Page < Pharmacy > search(
		String keyword,
		PharmacyType type,
		PharmacyStatus status,
		Boolean verified,
		Boolean open24Hours,
		Boolean offersDelivery,
		Boolean acceptsOnlineOrders,
		Instant from,
		Instant to,
		Pageable pageable
);
}