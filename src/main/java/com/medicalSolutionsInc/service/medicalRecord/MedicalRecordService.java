package com.medicalSolutionsInc.service.medicalRecord;

import com.medicalSolutionsInc.dto.medicalRecordDTO.MedicalRecordRequestDTO;
import com.medicalSolutionsInc.dto.medicalRecordDTO.MedicalRecordResponseDTO;
import com.medicalSolutionsInc.entity.medicalRecord.MedicalRecord;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.mappers.medicalRecordMapper.MedicalRecordMapper;
import com.medicalSolutionsInc.repository.medicalRecord.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

		private final MedicalRecordRepository medicalRecordRepository;
		private final MedicalRecordMapper medicalRecordMapper;
		
		public MedicalRecordResponseDTO addMedicalRecord(MedicalRecordRequestDTO request) {
			log.info("Creating medical record for patient: {}", request.patientId());
			
			MedicalRecord record = medicalRecordMapper.toEntity(request);
			MedicalRecord saved = medicalRecordRepository.save(record);
			
			log.info("Medical record created with ID: {}", saved.getId());
			return medicalRecordMapper.toResponseDTO(saved);
		}
		
		public Page<MedicalRecordResponseDTO> fetchMedicalRecords(Pageable pageable) {
			log.info("Fetching all medical records — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
			return medicalRecordRepository.findByDeletedAtIsNull(pageable)
					       .map(medicalRecordMapper::toResponseDTO);
		}
		
		public MedicalRecordResponseDTO fetchMedicalRecord(String id) throws Exception {
			log.info("Fetching medical record with ID: {}", id);
			MedicalRecord record = medicalRecordRepository.findByIdAndDeletedAtIsNull(id)
					                       .orElseThrow(() -> new NotFoundException("Medical record not found with ID: " + id));
			return medicalRecordMapper.toResponseDTO(record);
		}
		
		public MedicalRecordResponseDTO updateMedicalRecord(String id, MedicalRecordRequestDTO request) throws Exception {
			log.info("Updating medical record with ID: {}", id);
			
			MedicalRecord record = medicalRecordRepository.findByIdAndDeletedAtIsNull(id)
					                       .orElseThrow(() -> new NotFoundException("Medical record not found with ID: " + id));
			
			medicalRecordMapper.updateEntityFromDTO(request, record);
			MedicalRecord updated = medicalRecordRepository.save(record);
			
			log.info("Medical record updated successfully: {}", id);
			return medicalRecordMapper.toResponseDTO(updated);
		}
		
		public void deleteMedicalRecord(String id) throws  Exception {
			log.info("Soft-deleting medical record with ID: {}", id);
			
			MedicalRecord record = medicalRecordRepository.findByIdAndDeletedAtIsNull(id)
					                       .orElseThrow(() -> new NotFoundException ("Medical record not found with ID: " + id));
			
			record.setDeletedAt(Instant.now());
			medicalRecordRepository.save(record);
			log.info("Medical record soft-deleted successfully: {}", id);
		}
		
		public long countMedicalRecords() {
			log.info("Counting all active medical records");
			return medicalRecordRepository.countByDeletedAtIsNull();
		}
		
		public Page<MedicalRecordResponseDTO> searchMedicalRecords(
				String keyword,
				MedicalRecordStatus status,
				MedicalRecordType type,
				String patientId,
				String attendingDoctorId,
				String facilityId,
				Instant from,
				Instant to,
				Pageable pageable
		) {
			log.info("Searching medical records — keyword: '{}', status: {}, type: {}", keyword, status, type);
			return medicalRecordRepository.search(keyword, status, type, patientId, attendingDoctorId, facilityId, from, to, pageable)
					       .map(medicalRecordMapper::toResponseDTO);
		}
}