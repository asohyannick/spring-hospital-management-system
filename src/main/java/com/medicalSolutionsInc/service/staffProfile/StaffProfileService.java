package com.medicalSolutionsInc.service.staffProfile;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.dto.staffProfileDTO.CreateStaffProfileRequestDTO;
import com.medicalSolutionsInc.dto.staffProfileDTO.CreateStaffProfileResponseDTO;
import com.medicalSolutionsInc.entity.staffProfile.StaffProfile;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
import com.medicalSolutionsInc.exceptions.badRequestException.BadRequestException;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.mappers.staffProfileMapper.StaffProfileMapper;
import com.medicalSolutionsInc.repository.staffProfile.StaffProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class StaffProfileService {

		private final StaffProfileRepository staffProfileRepository;
		private final StaffProfileMapper staffProfileMapper;
		private final CloudinaryConfig cloudinaryConfig;
		
		public CreateStaffProfileResponseDTO addStaffProfile(
				CreateStaffProfileRequestDTO request,
				MultipartFile image
		) throws Exception {
			log.info("Creating staff profile for: {} {}", request.firstName(), request.lastName());
			
			if (staffProfileRepository.existsByEmail(request.email())) {
				throw new BadRequestException("A staff member with email '" + request.email() + "' already exists");
			}
			
			if (request.licenseNumber() != null &&
					    staffProfileRepository.existsByLicenseNumber(request.licenseNumber())) {
				throw new BadRequestException("A staff member with license number '" + request.licenseNumber() + "' already exists");
			}
			
			StaffProfile staff = staffProfileMapper.toEntity(request);
			
			if (image != null && !image.isEmpty()) {
				String imageUrl = cloudinaryConfig.uploadImage(
						image,
						"staff-profiles",
						"staff_" + UUID.randomUUID(),
						"Staff profile image"
				);
				staff.setImageUrl(imageUrl);
			}
			
			StaffProfile saved = staffProfileRepository.save(staff);
			log.info("Staff profile created with ID: {}", saved.getId());
			return staffProfileMapper.toResponseDTO(saved);
		}
		
		public Page<CreateStaffProfileResponseDTO> fetchStaffProfiles(Pageable pageable) {
			log.info("Fetching all staff profiles — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
			return staffProfileRepository.findByDeletedAtIsNull(pageable)
					       .map(staffProfileMapper::toResponseDTO);
		}
		
		public CreateStaffProfileResponseDTO fetchStaffProfile(String id) throws Exception {
			log.info("Fetching staff profile with ID: {}", id);
			StaffProfile staff = staffProfileRepository.findByIdAndDeletedAtIsNull(id)
					                     .orElseThrow(() -> new NotFoundException("Staff profile not found with ID: " + id));
			return staffProfileMapper.toResponseDTO(staff);
		}
		
		public CreateStaffProfileResponseDTO updateStaffProfile(
				String id,
				CreateStaffProfileRequestDTO request,
				MultipartFile image
		) throws Exception {
			log.info("Updating staff profile with ID: {}", id);
			
			StaffProfile staff = staffProfileRepository.findByIdAndDeletedAtIsNull(id)
					                     .orElseThrow(() -> new NotFoundException("Staff profile not found with ID: " + id));
			
			if (!staff.getEmail().equals(request.email()) &&
					    staffProfileRepository.existsByEmail(request.email())) {
				throw new BadRequestException("A staff member with email '" + request.email() + "' already exists");
			}
			
			if (request.licenseNumber() != null &&
					    !request.licenseNumber().equals(staff.getLicenseNumber()) &&
					    staffProfileRepository.existsByLicenseNumber(request.licenseNumber())) {
				throw new BadRequestException("A staff member with license number '" + request.licenseNumber() + "' already exists");
			}
			
			if (image != null && !image.isEmpty()) {
				if (staff.getImageUrl() != null && !staff.getImageUrl().isBlank()) {
					cloudinaryConfig.deleteByUrl(staff.getImageUrl());
				}
				String newImageUrl = cloudinaryConfig.uploadImage(
						image,
						"staff-profiles",
						"staff_" + UUID.randomUUID(),
						"Staff profile image"
				);
				staff.setImageUrl(newImageUrl);
			}
			
			staffProfileMapper.updateEntityFromDTO(request, staff);
			StaffProfile updated = staffProfileRepository.save(staff);
			log.info("Staff profile updated successfully: {}", id);
			return staffProfileMapper.toResponseDTO(updated);
		}
		
		public void deleteStaffProfile(String id) throws Exception {
			log.info("Soft-deleting staff profile with ID: {}", id);
			StaffProfile staff = staffProfileRepository.findByIdAndDeletedAtIsNull(id)
					                     .orElseThrow(() -> new NotFoundException ("Staff profile not found with ID: " + id));
			
			staff.setDeletedAt(Instant.now());
			staffProfileRepository.save(staff);
			log.info("Staff profile soft-deleted successfully: {}", id);
		}
		
		public long countStaffProfiles() {
			log.info("Counting all active staff profiles");
			return staffProfileRepository.countByDeletedAtIsNull();
		}
		
		public Page<CreateStaffProfileResponseDTO> searchStaffProfiles(
				String keyword,
				StaffRole role,
				GenderType gender,
				EmploymentStatus employmentStatus,
				String department,
				String facilityId,
				Boolean verified,
				Boolean available,
				Instant from,
				Instant to,
				Pageable pageable
		) {
			log.info("Searching staff profiles — keyword: '{}', role: {}, department: {}", keyword, role, department);
			return staffProfileRepository.search(
					keyword, role, gender, employmentStatus,
					department, facilityId, verified, available,
					from, to, pageable
			).map(staffProfileMapper::toResponseDTO);
		}
}