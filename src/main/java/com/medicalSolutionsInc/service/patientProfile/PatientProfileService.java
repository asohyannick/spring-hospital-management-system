package com.medicalSolutionsInc.service.patientProfile;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.dto.patientProfileDTO.CreatePatientProfileRequestDTO;
import com.medicalSolutionsInc.dto.patientProfileDTO.CreatePatientProfileResponseDTO;
import com.medicalSolutionsInc.entity.patientProfile.PatientProfile;
import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import com.medicalSolutionsInc.exceptions.badRequestException.BadRequestException;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.mappers.patientProfileMapper.PatientProfileMapper;
import com.medicalSolutionsInc.repository.patientProfile.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientProfileService {

		private final PatientProfileRepository patientProfileRepository;
		private final PatientProfileMapper patientProfileMapper;
		private final CloudinaryConfig cloudinaryConfig;
		
		public CreatePatientProfileResponseDTO addPatientProfile(
				CreatePatientProfileRequestDTO request,
				MultipartFile image
		) throws Exception {
			log.info("Creating patient profile for: {} {}", request.firstName(), request.lastName());
			
			if (patientProfileRepository.existsByEmail(request.email())) {
				throw new BadRequestException("A patient with email '" + request.email() + "' already exists");
			}
			
			if (request.nationalId() != null && patientProfileRepository.existsByNationalId(request.nationalId())) {
				throw new BadRequestException("A patient with national ID '" + request.nationalId() + "' already exists");
			}
			
			PatientProfile patient = patientProfileMapper.toEntity(request);
			
			if (image != null && !image.isEmpty()) {
				String imageUrl = cloudinaryConfig.uploadImage(
						image,
						"patient-profiles",
						"patient_" + UUID.randomUUID(),
						"Patient profile image"
				);
				patient.setImageUrl(imageUrl);
			}
			
			PatientProfile saved = patientProfileRepository.save(patient);
			log.info("Patient profile created with ID: {}", saved.getId());
			return patientProfileMapper.toResponseDTO(saved);
		}
		
		public Page<CreatePatientProfileResponseDTO> fetchPatientProfiles(Pageable pageable) {
			log.info("Fetching all patient profiles — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
			return patientProfileRepository.findByDeletedAtIsNull(pageable)
					       .map(patientProfileMapper::toResponseDTO);
		}
		
		public CreatePatientProfileResponseDTO fetchPatientProfile(String id) throws Exception {
			log.info("Fetching patient profile with ID: {}", id);
			PatientProfile patient = patientProfileRepository.findByIdAndDeletedAtIsNull(id)
					                         .orElseThrow(() -> new NotFoundException("Patient profile not found with ID: " + id));
			return patientProfileMapper.toResponseDTO(patient);
		}
		
		public CreatePatientProfileResponseDTO updatePatientProfile(
				String id,
				CreatePatientProfileRequestDTO request,
				MultipartFile image
		) throws Exception {
			log.info("Updating patient profile with ID: {}", id);
			
			PatientProfile patient = patientProfileRepository.findByIdAndDeletedAtIsNull(id)
					                         .orElseThrow(() -> new NotFoundException ("Patient profile not found with ID: " + id));
			
			if (!patient.getEmail().equals(request.email()) &&
					    patientProfileRepository.existsByEmail(request.email())) {
				throw new BadRequestException("A patient with email '" + request.email() + "' already exists");
			}
			
			if (request.nationalId() != null &&
					    !request.nationalId().equals(patient.getNationalId()) &&
					    patientProfileRepository.existsByNationalId(request.nationalId())) {
				throw new BadRequestException("A patient with national ID '" + request.nationalId() + "' already exists");
			}
			
			if (image != null && !image.isEmpty()) {
				if (patient.getImageUrl() != null && !patient.getImageUrl().isBlank()) {
					cloudinaryConfig.deleteByUrl(patient.getImageUrl());
				}
				String newImageUrl = cloudinaryConfig.uploadImage(
						image,
						"patient-profiles",
						"patient_" + UUID.randomUUID(),
						"Patient profile image"
				);
				patient.setImageUrl(newImageUrl);
			}
			
			patientProfileMapper.updateEntityFromDTO(request, patient);
			PatientProfile updated = patientProfileRepository.save(patient);
			log.info("Patient profile updated successfully: {}", id);
			return patientProfileMapper.toResponseDTO(updated);
		}
		
		public void deletePatientProfile(String id) throws Exception {
			log.info("Soft-deleting patient profile with ID: {}", id);
			PatientProfile patient = patientProfileRepository.findByIdAndDeletedAtIsNull(id)
					                         .orElseThrow(() -> new NotFoundException("Patient profile not found with ID: " + id));
			
			patient.setDeletedAt(Instant.now());
			patientProfileRepository.save(patient);
			log.info("Patient profile soft-deleted successfully: {}", id);
		}
		
		public long countPatientProfiles() {
			log.info("Counting all active patient profiles");
			return patientProfileRepository.countByDeletedAtIsNull();
		}
		
		public Page<CreatePatientProfileResponseDTO> searchPatientProfiles(
				String keyword,
				GenderType gender,
				BloodGroup bloodGroup,
				MaritalStatus maritalStatus,
				String primaryDoctorId,
				Boolean active,
				Boolean deceased,
				Instant from,
				Instant to,
				Pageable pageable
		) {
			log.info("Searching patient profiles — keyword: '{}', gender: {}, bloodGroup: {}", keyword, gender, bloodGroup);
			return patientProfileRepository.search(
					keyword, gender, bloodGroup, maritalStatus,
					primaryDoctorId, active, deceased, from, to, pageable
			).map(patientProfileMapper::toResponseDTO);
		}
}