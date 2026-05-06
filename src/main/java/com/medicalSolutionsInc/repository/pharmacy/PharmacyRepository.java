package com.medicalSolutionsInc.repository.pharmacy;

import com.medicalSolutionsInc.entity.pharmacy.Pharmacy;
import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PharmacyRepository extends MongoRepository<Pharmacy, String> {

		Optional<Pharmacy> findByPharmacyNumber(String pharmacyNumber);
		
		Optional<Pharmacy> findByLicenseNumber(String licenseNumber);
		
		Optional<Pharmacy> findByEmail(String email);
		
		boolean existsByLicenseNumber(String licenseNumber);
		
		boolean existsByEmail(String email);
		
		Page<Pharmacy> findByStatus(PharmacyStatus status, Pageable pageable);
		
		Page<Pharmacy> findByType(PharmacyType type, Pageable pageable);
		
		Page<Pharmacy> findByTypeAndStatus(PharmacyType type, PharmacyStatus status, Pageable pageable);
		
		List<Pharmacy> findByVerifiedTrue();
		
		List<Pharmacy> findByOffersDeliveryTrue();
		
		List<Pharmacy> findByOpen24HoursTrue();
		
		List<Pharmacy> findByAcceptsOnlineOrdersTrue();
		
		@Query("{ 'address.city': ?0, 'deleted_at': null }")
		Page<Pharmacy> findActiveByCity(String city, Pageable pageable);
		
		@Query("{ 'address.country': ?0, 'deleted_at': null }")
		Page<Pharmacy> findActiveByCountry(String country, Pageable pageable);
		
		@Query("{ 'total_medications_in_stock': { $lte: '$low_stock_threshold' }, 'deleted_at': null }")
		List<Pharmacy> findPharmaciesWithLowStock();
		
		@Query("{ 'deleted_at': null }")
		Page<Pharmacy> findAllActive(Pageable pageable);
}