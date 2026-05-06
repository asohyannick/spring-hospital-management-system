package com.medicalSolutionsInc.repository.pharmacy;

import com.medicalSolutionsInc.entity.pharmacy.Pharmacy;
import com.medicalSolutionsInc.utils.pharmacyRepositoryCustom.PharmacyRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacyRepository extends MongoRepository<Pharmacy, String>, PharmacyRepositoryCustom {
	
	boolean existsByEmail ( String email );
	
	boolean existsByLicenseNumber ( String licenseNumber );
	
	Page < Pharmacy > findByDeletedAtIsNull ( Pageable pageable );
	
	Optional < Pharmacy > findByIdAndDeletedAtIsNull ( String id );
	
	long countByDeletedAtIsNull ( );
	
}