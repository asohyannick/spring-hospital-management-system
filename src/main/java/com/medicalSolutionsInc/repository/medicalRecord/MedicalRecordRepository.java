package com.medicalSolutionsInc.repository.medicalRecord;
import com.medicalSolutionsInc.entity.medicalRecord.MedicalRecord;
import com.medicalSolutionsInc.utils.medicalRecordHelper.MedicalRecordRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String>, MedicalRecordRepositoryCustom {
		
		Page <MedicalRecord> findByDeletedAtIsNull( Pageable pageable);
		Optional<MedicalRecord> findByIdAndDeletedAtIsNull(String id);
		long countByDeletedAtIsNull();
}