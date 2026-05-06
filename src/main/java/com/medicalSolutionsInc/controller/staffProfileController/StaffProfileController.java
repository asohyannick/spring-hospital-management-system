package com.medicalSolutionsInc.controller.staffProfileController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.staffProfileDTO.CreateStaffProfileRequestDTO;
import com.medicalSolutionsInc.dto.staffProfileDTO.CreateStaffProfileResponseDTO;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
import com.medicalSolutionsInc.service.staffProfile.StaffProfileService;
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
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/${api.version}/staff-profile")
@RequiredArgsConstructor
@Tag(name = "Staff Profile Management Endpoints", description = "Endpoints for creating, retrieving, searching, and managing staff profiles")
public class StaffProfileController {

		private final StaffProfileService staffProfileService;
		
		@Operation(
				summary = "Create a new staff profile",
				description = "Creates a new staff profile. Accepts multipart/form-data with an optional image upload.",
				responses = {
						@ApiResponse(responseCode = "201", description = "Staff profile created successfully",
								content = @Content(schema = @Schema(implementation = CreateStaffProfileResponseDTO.class))),
						@ApiResponse(responseCode = "400", description = "Validation error or duplicate entry"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreateStaffProfileResponseDTO>> addStaffProfile(
				@RequestPart("data") @Valid CreateStaffProfileRequestDTO request,
				@RequestPart(value = "image", required = false) MultipartFile image
		) throws Exception {
			CreateStaffProfileResponseDTO data = staffProfileService.addStaffProfile(request, image);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseConfig<>(
					Instant.now(),
					"Staff profile created successfully",
					data,
					HttpStatus.CREATED.value()
			));
		}
		
		@Operation(
				summary = "Fetch all staff profiles",
				description = "Returns a paginated and sorted list of all non-deleted staff profiles.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Staff profiles retrieved successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping
		public ResponseEntity<ApiResponseConfig<Page<CreateStaffProfileResponseDTO>>> fetchStaffProfiles(
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreateStaffProfileResponseDTO> data = staffProfileService.fetchStaffProfiles(
					buildPageable(page, size, sortBy, direction)
			);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body (
					new ApiResponseConfig<>(
							Instant.now(),
							"Staff profiles retrieved successfully",
							data,
							HttpStatus.OK.value()
					)
			);
		}
		
		@Operation(
				summary = "Fetch a staff profile by ID",
				description = "Returns the full details of a single staff profile.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Staff profile found"),
						@ApiResponse(responseCode = "404", description = "Staff profile not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<CreateStaffProfileResponseDTO>> fetchStaffProfile(
				@Parameter(description = "Staff profile ID") @PathVariable String id
		) throws Exception {
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body (
					new ApiResponseConfig<>(
							Instant.now(),
							"Staff profile retrieved successfully",
							staffProfileService.fetchStaffProfile(id),
							HttpStatus.OK.value()
					)
			);
		}
		
		@Operation(
				summary = "Update a staff profile",
				description = "Updates an existing staff profile. Optionally replaces the image — old image is deleted from Cloudinary if a new one is provided.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Staff profile updated successfully"),
						@ApiResponse(responseCode = "400", description = "Validation error or duplicate entry"),
						@ApiResponse(responseCode = "404", description = "Staff profile not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreateStaffProfileResponseDTO>> updateStaffProfile(
				@Parameter(description = "Staff profile ID") @PathVariable String id,
				@RequestPart("data") @Valid CreateStaffProfileRequestDTO request,
				@RequestPart(value = "image", required = false) MultipartFile image
		) throws Exception {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Staff profile updated successfully",
					staffProfileService.updateStaffProfile(id, request, image),
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Soft-delete a staff profile",
				description = "Marks a staff profile as deleted without removing it from the database.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Staff profile deleted successfully"),
						@ApiResponse(responseCode = "404", description = "Staff profile not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@DeleteMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<Void>> deleteStaffProfile(
				@Parameter(description = "Staff profile ID") @PathVariable String id
		) throws  Exception {
			staffProfileService.deleteStaffProfile(id);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Staff profile deleted successfully",
					null,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Count all staff profiles",
				description = "Returns the total number of active (non-deleted) staff profiles.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Count returned successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/count")
		public ResponseEntity<ApiResponseConfig<Map<String, Long>>> countStaffProfiles() {
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body (
							       new ApiResponseConfig<>(
									       Instant.now(),
									       "Staff profile count retrieved successfully",
									       Map.of("count", staffProfileService.countStaffProfiles()),
									       HttpStatus.OK.value()
							       )
					       );
		}
		
		@Operation(
				summary = "Search staff profiles",
				description = "Search and filter staff profiles by keyword, role, gender, employment status, department, facility, verified, available, and date range. Supports sorting and pagination.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Search results returned"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/search")
		public ResponseEntity<ApiResponseConfig<Page<CreateStaffProfileResponseDTO>>> searchStaffProfiles(
				@Parameter(description = "Keyword (matches name, email, phone, staff number, license number, department, specialization)")
				@RequestParam(required = false) String keyword,
				
				@Parameter(description = "Filter by staff role")
				@RequestParam(required = false) StaffRole role,
				
				@Parameter(description = "Filter by gender")
				@RequestParam(required = false) GenderType gender,
				
				@Parameter(description = "Filter by employment status")
				@RequestParam(required = false) EmploymentStatus employmentStatus,
				
				@Parameter(description = "Filter by department")
				@RequestParam(required = false) String department,
				
				@Parameter(description = "Filter by facility ID")
				@RequestParam(required = false) String facilityId,
				
				@Parameter(description = "Filter by verification status")
				@RequestParam(required = false) Boolean verified,
				
				@Parameter(description = "Filter by availability")
				@RequestParam(required = false) Boolean available,
				
				@Parameter(description = "Filter profiles created from this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
				
				@Parameter(description = "Filter profiles created up to this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
				
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreateStaffProfileResponseDTO> data = staffProfileService.searchStaffProfiles(
					keyword, role, gender, employmentStatus,
					department, facilityId, verified, available,
					from, to, buildPageable(page, size, sortBy, direction)
			);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body(
					new ApiResponseConfig<>(
							Instant.now(),
							"Search completed successfully",
							data,
							HttpStatus.OK.value()
					)
			);
		}
		
		private Pageable buildPageable(int page, int size, String sortBy, String direction) {
			Sort sort = direction.equalsIgnoreCase("asc")
					            ? Sort.by(sortBy).ascending()
					            : Sort.by(sortBy).descending();
			return PageRequest.of(page, size, sort);
		}
}