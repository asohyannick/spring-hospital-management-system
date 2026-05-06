package com.medicalSolutionsInc.service.laboratory;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.dto.laboratoryDTO.LaboratoryRequestDTO;
import com.medicalSolutionsInc.dto.laboratoryDTO.LaboratoryResponseDTO;
import com.medicalSolutionsInc.entity.laboratory.Laboratory;
import com.medicalSolutionsInc.exceptions.badRequestException.BadRequestException;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.mappers.laboratoryMapper.LaboratoryMapper;
import com.medicalSolutionsInc.repository.laboratory.LaboratoryRepository;
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
public class LaboratoryService {

private final LaboratoryRepository laboratoryRepository;
private final LaboratoryMapper laboratoryMapper;
private final CloudinaryConfig cloudinaryConfig;
		
		public LaboratoryResponseDTO addLaboratory(LaboratoryRequestDTO request, MultipartFile image) throws Exception {
			log.info("Creating new laboratory: {}", request.name());
			
			if (laboratoryRepository.existsByEmail(request.email())) {
				throw new BadRequestException("A laboratory with email '" + request.email() + "' already exists");
			}
			
			if (laboratoryRepository.existsByRegistrationNumber(request.registrationNumber())) {
				throw new BadRequestException("A laboratory with registration number '" + request.registrationNumber() + "' already exists");
			}
			
			Laboratory laboratory = laboratoryMapper.toEntity(request);
			
			if (image != null && !image.isEmpty()) {
				String imageUrl = cloudinaryConfig.uploadImage(
						image,
						"laboratories",
						"lab_" + UUID.randomUUID(),
						"Laboratory image"
				);
				laboratory.setImageUrl(imageUrl);
			}
			
			Laboratory saved = laboratoryRepository.save(laboratory);
			log.info("Laboratory created successfully with ID: {}", saved.getId());
			return laboratoryMapper.toResponseDTO(saved);
		}
		
		public LaboratoryResponseDTO updateLaboratory(String id, LaboratoryRequestDTO request, MultipartFile image) throws Exception {
			log.info("Updating laboratory with ID: {}", id);
			
			Laboratory laboratory = laboratoryRepository.findByIdAndDeletedAtIsNull(id)
					                        .orElseThrow(() -> new NotFoundException("Laboratory not found with ID: " + id));
			
			if (!laboratory.getEmail().equals(request.email()) && laboratoryRepository.existsByEmail(request.email())) {
				throw new BadRequestException("A laboratory with email '" + request.email() + "' already exists");
			}
			
			if (!laboratory.getRegistrationNumber().equals(request.registrationNumber()) &&
					    laboratoryRepository.existsByRegistrationNumber(request.registrationNumber())) {
				throw new BadRequestException("A laboratory with registration number '" + request.registrationNumber() + "' already exists");
			}
			
			if (image != null && !image.isEmpty()) {
				if (laboratory.getImageUrl() != null && !laboratory.getImageUrl().isBlank()) {
					cloudinaryConfig.deleteByUrl(laboratory.getImageUrl());
				}
				String newImageUrl = cloudinaryConfig.uploadImage(
						image,
						"laboratories",
						"lab_" + UUID.randomUUID(),
						"Laboratory image"
				);
				laboratory.setImageUrl(newImageUrl);
			}
			
			laboratoryMapper.updateEntityFromDTO(request, laboratory);
			Laboratory updated = laboratoryRepository.save(laboratory);
			log.info("Laboratory updated successfully: {}", id);
			return laboratoryMapper.toResponseDTO(updated);
		}
		
		public Page<LaboratoryResponseDTO> fetchLaboratories(Pageable pageable) {
			log.info("Fetching all laboratories — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
			return laboratoryRepository.findByDeletedAtIsNull(pageable)
					       .map(laboratoryMapper::toResponseDTO);
		}
		
		public LaboratoryResponseDTO fetchLaboratory(String id) throws  Exception {
			log.info("Fetching laboratory with ID: {}", id);
			Laboratory laboratory = laboratoryRepository.findByIdAndDeletedAtIsNull(id)
					                        .orElseThrow(() -> new NotFoundException ("Laboratory not found with ID: " + id));
			return laboratoryMapper.toResponseDTO(laboratory);
		}
		
		public void deleteLaboratory(String id) throws Exception {
			log.info("Soft-deleting laboratory with ID: {}", id);
			Laboratory laboratory = laboratoryRepository.findByIdAndDeletedAtIsNull(id)
					                        .orElseThrow(() -> new NotFoundException("Laboratory not found with ID: " + id));
			
			laboratory.setDeletedAt(Instant.now());
			laboratoryRepository.save(laboratory);
			log.info("Laboratory soft-deleted successfully: {}", id);
		}


		public Page<LaboratoryResponseDTO> searchLaboratories(String keyword, Pageable pageable) {
			log.info("Searching laboratories with keyword: '{}'", keyword);
			return laboratoryRepository
					       .searchByKeyword(keyword, pageable)
					       .map(laboratoryMapper::toResponseDTO);
		}
		public long countLaboratories() {
			log.info("Counting all active laboratories");
			return laboratoryRepository.countByDeletedAtIsNull();
		}
}