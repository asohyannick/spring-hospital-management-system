package com.medicalSolutionsInc.controller.pharmacyController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.pharmacyDTO.CreatePharmacyRequestDTO;
import com.medicalSolutionsInc.dto.pharmacyDTO.CreatePharmacyResponseDTO;
import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
import com.medicalSolutionsInc.service.pharmacy.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/pharmacy")
@Tag(name = "Pharmacy Management Endpoints", description = "Endpoints for creating, retrieving, searching, and managing pharmacies")
public class PharmacyController {
		
		private final PharmacyService pharmacyService;
		
		@Operation(
				summary = "Create a new pharmacy",
				description = "Creates a new pharmacy record. Accepts multipart/form-data with an optional image upload.",
				responses = {
						@ApiResponse(responseCode = "201", description = "Pharmacy created successfully",
								content = @Content(schema = @Schema(implementation = CreatePharmacyResponseDTO.class))),
						@ApiResponse(responseCode = "400", description = "Validation error or duplicate entry"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreatePharmacyResponseDTO>> addPharmacy(
				@RequestPart("data") @Valid CreatePharmacyRequestDTO request,
				@RequestPart(value = "image", required = false) MultipartFile image
		) throws Exception {
			CreatePharmacyResponseDTO data = pharmacyService.addPharmacy(request, image);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseConfig<>(
					Instant.now(),
					"Pharmacy created successfully",
					data,
					HttpStatus.CREATED.value()
			));
		}
		
		@Operation(
				summary = "Fetch all pharmacies",
				description = "Returns a paginated and sorted list of all non-deleted pharmacies.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Pharmacies retrieved successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping
		public ResponseEntity<ApiResponseConfig<Page<CreatePharmacyResponseDTO>>> fetchPharmacies(
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreatePharmacyResponseDTO> data = pharmacyService.fetchPharmacies(buildPageable(page, size, sortBy, direction));
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Pharmacies retrieved successfully",
					data,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Fetch a pharmacy by ID",
				description = "Returns the full details of a single pharmacy.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Pharmacy found"),
						@ApiResponse(responseCode = "404", description = "Pharmacy not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<CreatePharmacyResponseDTO>> fetchPharmacy(
				@Parameter(description = "Pharmacy ID") @PathVariable String id
		) {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Pharmacy retrieved successfully",
					pharmacyService.fetchPharmacy(id),
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Update a pharmacy",
				description = "Updates an existing pharmacy. Optionally replaces the image — old image is deleted from Cloudinary if a new one is provided.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Pharmacy updated successfully"),
						@ApiResponse(responseCode = "400", description = "Validation error or duplicate entry"),
						@ApiResponse(responseCode = "404", description = "Pharmacy not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreatePharmacyResponseDTO>> updatePharmacy(
				@Parameter(description = "Pharmacy ID") @PathVariable String id,
				@RequestPart("data") @Valid CreatePharmacyRequestDTO request,
				@RequestPart(value = "image", required = false) MultipartFile image
		) throws Exception {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Pharmacy updated successfully",
					pharmacyService.updatePharmacy(id, request, image),
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Soft-delete a pharmacy",
				description = "Marks a pharmacy as deleted without removing it from the database.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Pharmacy deleted successfully"),
						@ApiResponse(responseCode = "404", description = "Pharmacy not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@DeleteMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<Void>> deletePharmacy(
				@Parameter(description = "Pharmacy ID") @PathVariable String id
		) {
			pharmacyService.deletePharmacy(id);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Pharmacy deleted successfully",
					null,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Count all pharmacies",
				description = "Returns the total number of active (non-deleted) pharmacies.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Count returned successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/count")
		public ResponseEntity<ApiResponseConfig<Map<String, Long>>> countPharmacies() {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Pharmacy count retrieved successfully",
					Map.of("count", pharmacyService.countPharmacies()),
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Search pharmacies",
				description = "Search and filter pharmacies by keyword, type, status, verified, open 24h, delivery, online orders, and date range. Supports sorting and pagination.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Search results returned"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/search")
		public ResponseEntity<ApiResponseConfig<Page<CreatePharmacyResponseDTO>>> searchPharmacies(
				@Parameter(description = "Keyword (matches name, email, license number, pharmacy number, head pharmacist)")
				@RequestParam(required = false) String keyword,
				
				@Parameter(description = "Filter by pharmacy type")
				@RequestParam(required = false) PharmacyType type,
				
				@Parameter(description = "Filter by pharmacy status")
				@RequestParam(required = false) PharmacyStatus status,
				
				@Parameter(description = "Filter by verified status")
				@RequestParam(required = false) Boolean verified,
				
				@Parameter(description = "Filter by 24-hour availability")
				@RequestParam(required = false) Boolean open24Hours,
				
				@Parameter(description = "Filter by delivery availability")
				@RequestParam(required = false) Boolean offersDelivery,
				
				@Parameter(description = "Filter by online order availability")
				@RequestParam(required = false) Boolean acceptsOnlineOrders,
				
				@Parameter(description = "Filter pharmacies created from this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
				
				@Parameter(description = "Filter pharmacies created up to this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
				
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreatePharmacyResponseDTO> data = pharmacyService.searchPharmacies(
					keyword, type, status, verified,
					open24Hours, offersDelivery, acceptsOnlineOrders,
					from, to, buildPageable(page, size, sortBy, direction)
			);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Search completed successfully",
					data,
					HttpStatus.OK.value()
			));
		}
		
		private Pageable buildPageable(int page, int size, String sortBy, String direction) {
			Sort sort = direction.equalsIgnoreCase("asc")
					            ? Sort.by(sortBy).ascending()
					            : Sort.by(sortBy).descending();
			return PageRequest.of(page, size, sort);
		}
}