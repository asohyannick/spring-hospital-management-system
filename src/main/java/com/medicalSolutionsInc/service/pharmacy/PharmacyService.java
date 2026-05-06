package com.medicalSolutionsInc.service.pharmacy;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.dto.pharmacyDTO.CreatePharmacyRequestDTO;
import com.medicalSolutionsInc.dto.pharmacyDTO.CreatePharmacyResponseDTO;
import com.medicalSolutionsInc.entity.pharmacy.Pharmacy;
import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
import com.medicalSolutionsInc.exceptions.badRequestException.BadRequestException;
import com.medicalSolutionsInc.mappers.pharmacyMapper.PharmacyMapper;
import com.medicalSolutionsInc.repository.pharmacy.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class PharmacyService {

private final PharmacyRepository pharmacyRepository;
private final PharmacyMapper pharmacyMapper;
private final CloudinaryConfig cloudinaryConfig;

// ─── Add Pharmacy ──────────────────────────────────────────────────────────

public CreatePharmacyResponseDTO addPharmacy(
		CreatePharmacyRequestDTO request,
		MultipartFile image
) throws Exception {
	log.info("Creating pharmacy: {}", request.name());
	
	if (pharmacyRepository.existsByEmail(request.email())) {
		throw new BadRequestException("A pharmacy with email '" + request.email() + "' already exists");
	}
	
	if (pharmacyRepository.existsByLicenseNumber(request.licenseNumber())) {
		throw new BadRequestException("A pharmacy with license number '" + request.licenseNumber() + "' already exists");
	}
	
	Pharmacy pharmacy = pharmacyMapper.toEntity(request);
	
	if (image != null && !image.isEmpty()) {
		String imageUrl = cloudinaryConfig.uploadImage(
				image,
				"pharmacies",
				"pharmacy_" + UUID.randomUUID(),
				"Pharmacy image"
		);
		pharmacy.setImageUrl(imageUrl);
	}
	
	Pharmacy saved = pharmacyRepository.save(pharmacy);
	log.info("Pharmacy created with ID: {}", saved.getId());
	return pharmacyMapper.toResponseDTO(saved);
}

// ─── Fetch All Pharmacies (Paginated) ──────────────────────────────────────

public Page<CreatePharmacyResponseDTO> fetchPharmacies(Pageable pageable) {
	log.info("Fetching all pharmacies — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
	return pharmacyRepository.findByDeletedAtIsNull(pageable)
			       .map(pharmacyMapper::toResponseDTO);
}

// ─── Fetch Single Pharmacy ─────────────────────────────────────────────────

public CreatePharmacyResponseDTO fetchPharmacy(String id) {
	log.info("Fetching pharmacy with ID: {}", id);
	Pharmacy pharmacy = pharmacyRepository.findByIdAndDeletedAtIsNull(id)
			                    .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found with ID: " + id));
	return pharmacyMapper.toResponseDTO(pharmacy);
}

// ─── Update Pharmacy ───────────────────────────────────────────────────────

public CreatePharmacyResponseDTO updatePharmacy(
		String id,
		CreatePharmacyRequestDTO request,
		MultipartFile image
) throws Exception {
	log.info("Updating pharmacy with ID: {}", id);
	
	Pharmacy pharmacy = pharmacyRepository.findByIdAndDeletedAtIsNull(id)
			                    .orElseThrow(() -> new NotFoundException("Pharmacy not found with ID: " + id));
	
	if (!pharmacy.getEmail().equals(request.email()) &&
			    pharmacyRepository.existsByEmail(request.email())) {
		throw new BadRequestException("A pharmacy with email '" + request.email() + "' already exists");
	}
	
	if (!pharmacy.getLicenseNumber().equals(request.licenseNumber()) &&
			    pharmacyRepository.existsByLicenseNumber(request.licenseNumber())) {
		throw new BadRequestException("A pharmacy with license number '" + request.licenseNumber() + "' already exists");
	}
	
	if (image != null && !image.isEmpty()) {
		if (pharmacy.getImageUrl() != null && !pharmacy.getImageUrl().isBlank()) {
			cloudinaryConfig.deleteByUrl(pharmacy.getImageUrl());
		}
		String newImageUrl = cloudinaryConfig.uploadImage(
				image,
				"pharmacies",
				"pharmacy_" + UUID.randomUUID(),
				"Pharmacy image"
		);
		pharmacy.setImageUrl(newImageUrl);
	}
	
	pharmacyMapper.updateEntityFromDTO(request, pharmacy);
	Pharmacy updated = pharmacyRepository.save(pharmacy);
	log.info("Pharmacy updated successfully: {}", id);
	return pharmacyMapper.toResponseDTO(updated);
}

// ─── Soft Delete Pharmacy ──────────────────────────────────────────────────

public void deletePharmacy(String id) {
	log.info("Soft-deleting pharmacy with ID: {}", id);
	Pharmacy pharmacy = pharmacyRepository.findByIdAndDeletedAtIsNull(id)
			                    .orElseThrow(() -> new NotFoundException("Pharmacy not found with ID: " + id));
	
	pharmacy.setDeletedAt(Instant.now());
	pharmacyRepository.save(pharmacy);
	log.info("Pharmacy soft-deleted successfully: {}", id);
}

// ─── Count Pharmacies ──────────────────────────────────────────────────────

public long countPharmacies() {
	log.info("Counting all active pharmacies");
	return pharmacyRepository.countByDeletedAtIsNull();
}

// ─── Search Pharmacies ─────────────────────────────────────────────────────

public Page<CreatePharmacyResponseDTO> searchPharmacies(
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
) {
	log.info("Searching pharmacies — keyword: '{}', type: {}, status: {}", keyword, type, status);
	return pharmacyRepository.search(
			keyword, type, status, verified,
			open24Hours, offersDelivery, acceptsOnlineOrders,
			from, to, pageable
	).map(pharmacyMapper::toResponseDTO);
}
}