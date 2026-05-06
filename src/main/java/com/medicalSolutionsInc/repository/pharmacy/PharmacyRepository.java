package com.medicalSolutionsInc.repository.pharmacy;

import com.medicalSolutionsInc.entity.pharmacy.Pharmacy;
import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PharmacyRepository extends MongoRepository<Pharmacy, String> {

		boolean existsByEmail(String email);
		boolean existsByLicenseNumber(String licenseNumber);
		
		Page<Pharmacy> findByDeletedAtIsNull(Pageable pageable);
		Optional<Pharmacy> findByIdAndDeletedAtIsNull(String id);
		long countByDeletedAtIsNull();
		
		@Query("""
		            {
		              "deleted_at": null,
		              "$and": [
		                { "$or": [
		                  { "$expr": { "$eq": [{ "$type": "?0" }, "missing"] } },
		                  { "name":            { "$regex": "?0", "$options": "i" } },
		                  { "email":           { "$regex": "?0", "$options": "i" } },
		                  { "license_number":  { "$regex": "?0", "$options": "i" } },
		                  { "pharmacy_number": { "$regex": "?0", "$options": "i" } },
		                  { "head_pharmacist": { "$regex": "?0", "$options": "i" } }
		                ]},
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?1" }, "missing"] } }, { "type": "?1" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?2" }, "missing"] } }, { "status": "?2" }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?3" }, "missing"] } }, { "verified": ?3 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?4" }, "missing"] } }, { "open_24_hours": ?4 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?5" }, "missing"] } }, { "offers_delivery": ?5 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?6" }, "missing"] } }, { "accepts_online_orders": ?6 }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?7" }, "missing"] } }, { "created_at": { "$gte": "?7" } }] },
		                { "$or": [{ "$expr": { "$eq": [{ "$type": "?8" }, "missing"] } }, { "created_at": { "$lte": "?8" } }] }
		              ]
		            }
		            """)
		Page<Pharmacy> search(
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