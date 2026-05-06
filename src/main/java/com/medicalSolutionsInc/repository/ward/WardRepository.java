package com.medicalSolutionsInc.repository.ward;

import com.medicalSolutionsInc.entity.ward.Ward;
import com.medicalSolutionsInc.enumerations.wardStatus.WardStatus;
import com.medicalSolutionsInc.enumerations.wardType.WardType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends MongoRepository<Ward, String> {

		Optional<Ward> findByWardNumber(String wardNumber);
		
		boolean existsByWardNumber(String wardNumber);
		
		List<Ward> findByFacilityId(String facilityId);
		
		List<Ward> findByFacilityIdAndStatus(String facilityId, WardStatus status);
		
		List<Ward> findByFacilityIdAndType(String facilityId, WardType type);
		
		List<Ward> findByStatus(WardStatus status);
		
		List<Ward> findByType(WardType type);
		
		List<Ward> findByIsolationWardTrue();
		
		List<Ward> findByPediatricTrue();
		
		List<Ward> findByAcceptsEmergencyTrue();
		
		List<Ward> findByAvailableBedsGreaterThan(int count);
		
		List<Ward> findByFacilityIdAndAvailableBedsGreaterThan(String facilityId, int count);
		
		List<Ward> findByDeletedAtIsNull();
		
		Optional<Ward> findByIdAndDeletedAtIsNull(String id);
		
		List<Ward> findByFacilityIdAndDeletedAtIsNull(String facilityId);
}