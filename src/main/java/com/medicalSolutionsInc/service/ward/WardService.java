package com.medicalSolutionsInc.service.ward;
import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.updateWardRequestDTO.UpdateWardRequestDTO;
import com.medicalSolutionsInc.dto.wardDTO.CreateWardRequestDTO;
import com.medicalSolutionsInc.dto.wardDTO.CreateWardResponseDTO;
import com.medicalSolutionsInc.entity.ward.Ward;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.mappers.wardMapper.WardMapper;
import com.medicalSolutionsInc.repository.ward.WardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WardService {

		private final WardRepository wardRepository;
		private final WardMapper wardMapper;
		private final MongoTemplate mongoTemplate;
		
		public ApiResponseConfig<CreateWardResponseDTO> addWard(CreateWardRequestDTO request) {
			log.info("Creating new ward with name: {}", request.name());
			
			Ward ward = wardMapper.toEntity(request);
			ward.setWardNumber(generateWardNumber());
			ward.setAvailableBeds(request.totalBeds());
			ward.setOccupiedBeds(0);
			ward.setReservedBeds(0);
			ward.setStaffCount(resolveStaffCount(request));
			ward.setCreatedAt(Instant.now());
			ward.setUpdatedAt(Instant.now());
			
			Ward saved = wardRepository.save(ward);
			log.info("Ward created successfully with ID: {}", saved.getId());
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Ward created successfully",
					wardMapper.toResponseDTO(saved),
					HttpStatus.CREATED.value()
			);
		}
		
		public ApiResponseConfig<List<CreateWardResponseDTO>> fetchWards() throws Exception {
			log.info("Fetching all active wards");
			
			List<CreateWardResponseDTO> wards = wardRepository.findByDeletedAtIsNull()
					                                    .stream()
					                                    .map(wardMapper::toResponseDTO)
					                                    .toList();
			
			log.info("Fetched {} wards", wards.size());
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Wards fetched successfully",
					wards,
					HttpStatus.OK.value()
			);
		}
		
		public ApiResponseConfig<CreateWardResponseDTO> fetchWard(String wardId) throws Exception {
			log.info("Fetching ward with ID: {}", wardId);
			
			Ward ward = findActiveWardOrThrow(wardId);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Ward fetched successfully",
					wardMapper.toResponseDTO(ward),
					HttpStatus.OK.value()
			);
		}
		
		public ApiResponseConfig<CreateWardResponseDTO> updateWard(String wardId, UpdateWardRequestDTO request) throws Exception {
			log.info("Updating ward with ID: {}", wardId);
			
			Ward ward = findActiveWardOrThrow(wardId);
			
			applyUpdates(ward, request);
			ward.setUpdatedAt(Instant.now());
			
			Ward updated = wardRepository.save(ward);
			log.info("Ward updated successfully with ID: {}", updated.getId());
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Ward updated successfully",
					wardMapper.toResponseDTO(updated),
					HttpStatus.OK.value()
			);
		}
		
		public ApiResponseConfig<Void> deleteWard(String wardId) throws Exception {
			log.info("Soft-deleting ward with ID: {}", wardId);
			
			Ward ward = findActiveWardOrThrow(wardId);
			ward.setDeletedAt(Instant.now());
			wardRepository.save(ward);
			
			log.info("Ward soft-deleted successfully with ID: {}", wardId);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Ward deleted successfully",
					null,
					HttpStatus.OK.value()
			);
		}
		
		public ApiResponseConfig<Long> countTotalWards() {
			log.info("Counting total active wards");
			
			long count = wardRepository.findByDeletedAtIsNull().size();
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Total ward count fetched successfully",
					count,
					HttpStatus.OK.value()
			);
		}
		
		public ApiResponseConfig<Page<CreateWardResponseDTO>> searchWards(
				String keyword,
				String type,
				String status,
				String floor,
				String building,
				Boolean isolationWard,
				Boolean privateWard,
				Boolean pediatric,
				Boolean acceptsEmergency,
				int page,
				int size,
				String sortBy,
				String sortDirection
		) {
			log.info("Searching wards — keyword: {}, type: {}, status: {}, page: {}, size: {}", keyword, type, status, page, size);
			
			Sort sort = sortDirection.equalsIgnoreCase("desc")
					            ? Sort.by(sortBy).descending()
					            : Sort.by(sortBy).ascending();
			
			Pageable pageable = PageRequest.of(page, size, sort);
			
			Query query = new Query();
			
			query.addCriteria(Criteria.where("deletedAt").isNull());
			
			if (StringUtils.hasText(keyword)) {
				String regex = ".*" + keyword.trim() + ".*";
				query.addCriteria(new Criteria().orOperator(
						Criteria.where("name").regex(regex, "i"),
						Criteria.where("wardNumber").regex(regex, "i"),
						Criteria.where("description").regex(regex, "i"),
						Criteria.where("building").regex(regex, "i"),
						Criteria.where("floor").regex(regex, "i")
				));
			}
			
			if (StringUtils.hasText(type))         query.addCriteria(Criteria.where("type").is(type.toUpperCase()));
			if (StringUtils.hasText(status))       query.addCriteria(Criteria.where("status").is(status.toUpperCase()));
			if (StringUtils.hasText(floor))        query.addCriteria(Criteria.where("floor").regex(floor, "i"));
			if (StringUtils.hasText(building))     query.addCriteria(Criteria.where("building").regex(building, "i"));
			if (isolationWard != null)             query.addCriteria(Criteria.where("isolationWard").is(isolationWard));
			if (privateWard != null)               query.addCriteria(Criteria.where("privateWard").is(privateWard));
			if (pediatric != null)                 query.addCriteria(Criteria.where("pediatric").is(pediatric));
			if (acceptsEmergency != null)          query.addCriteria(Criteria.where("acceptsEmergency").is(acceptsEmergency));
			
			long totalCount = mongoTemplate.count(query, Ward.class);
			
			query.with(pageable);
			List<Ward> wards = mongoTemplate.find(query, Ward.class);
			
			List<CreateWardResponseDTO> dtoList = wards.stream()
					                                      .map(wardMapper::toResponseDTO)
					                                      .toList();
			
			Page<CreateWardResponseDTO> resultPage = new PageImpl<>(dtoList, pageable, totalCount);
			
			log.info("Ward search returned {} results (total: {})", dtoList.size(), totalCount);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Wards search completed successfully",
					resultPage,
					HttpStatus.OK.value()
			);
		}
		
		private Ward findActiveWardOrThrow(String wardId) throws Exception {
			return wardRepository.findByIdAndDeletedAtIsNull(wardId)
					       .orElseThrow(() -> {
						       log.warn("Ward not found or deleted — ID: {}", wardId);
						       return new NotFoundException("Ward not found with ID: " + wardId);
					       });
		}
		
		private String generateWardNumber() {
			return "WRD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		}
		
		private int resolveStaffCount(CreateWardRequestDTO request) {
			int doctors = request.assignedDoctorIds() != null ? request.assignedDoctorIds().size() : 0;
			int nurses  = request.assignedNurseIds()  != null ? request.assignedNurseIds().size()  : 0;
			return doctors + nurses;
		}
		
		private void applyUpdates(Ward ward, UpdateWardRequestDTO request) {
			if (StringUtils.hasText(request.name()))           ward.setName(request.name());
			if (request.type() != null)                        ward.setType(request.type());
			if (request.status() != null)                      ward.setStatus(request.status());
			if (StringUtils.hasText(request.description()))    ward.setDescription(request.description());
			if (StringUtils.hasText(request.floor()))          ward.setFloor(request.floor());
			if (StringUtils.hasText(request.building()))       ward.setBuilding(request.building());
			if (StringUtils.hasText(request.facilityId()))     ward.setFacilityId(request.facilityId());
			if (StringUtils.hasText(request.facilityName()))   ward.setFacilityName(request.facilityName());
			if (request.totalBeds() != null)                   ward.setTotalBeds(request.totalBeds());
			if (request.beds() != null)                        ward.setBeds(request.beds().stream().map(wardMapper::toBed).toList());
			if (StringUtils.hasText(request.wardInChargeId())) ward.setWardInChargeId(request.wardInChargeId());
			if (StringUtils.hasText(request.wardInChargeName())) ward.setWardInChargeName(request.wardInChargeName());
			if (request.assignedDoctorIds() != null)           ward.setAssignedDoctorIds(request.assignedDoctorIds());
			if (request.assignedNurseIds() != null)            ward.setAssignedNurseIds(request.assignedNurseIds());
			if (request.equipment() != null)                   ward.setEquipment(request.equipment());
			if (request.amenities() != null)                   ward.setAmenities(request.amenities());
			if (request.genderRestriction() != null)           ward.setGenderRestriction(request.genderRestriction());
			if (request.visitingHours() != null)               ward.setVisitingHours(wardMapper.toVisitingHours(request.visitingHours()));
			if (request.isolationWard() != null)               ward.setIsolationWard(request.isolationWard());
			if (request.privateWard() != null)                 ward.setPrivateWard(request.privateWard());
			if (request.pediatric() != null)                   ward.setPediatric(request.pediatric());
			if (request.acceptsEmergency() != null)            ward.setAcceptsEmergency(request.acceptsEmergency());
			
			int doctors = ward.getAssignedDoctorIds() != null ? ward.getAssignedDoctorIds().size() : 0;
			int nurses  = ward.getAssignedNurseIds()  != null ? ward.getAssignedNurseIds().size()  : 0;
			ward.setStaffCount(doctors + nurses);
		}
}