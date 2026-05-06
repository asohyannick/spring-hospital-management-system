package com.medicalSolutionsInc.controller.patientProfileController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.patientProfileDTO.CreatePatientProfileRequestDTO;
import com.medicalSolutionsInc.dto.patientProfileDTO.CreatePatientProfileResponseDTO;
import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import com.medicalSolutionsInc.service.patientProfile.PatientProfileService;
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
@RequestMapping("/api/${api.version}/patient")
@Tag(name = "Patient Profile Management Endpoints", description = "Endpoints for creating, retrieving, searching, and managing patient profiles")
public class PatientProfileController {

		private final PatientProfileService patientProfileService;
		
		@Operation(
				summary = "Create a new patient profile",
				description = "Creates a new patient profile. Accepts multipart/form-data with an optional image upload.",
				responses = {
						@ApiResponse(responseCode = "201", description = "Patient profile created successfully",
								content = @Content(schema = @Schema(implementation = CreatePatientProfileResponseDTO.class))),
						@ApiResponse(responseCode = "400", description = "Validation error or duplicate entry"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreatePatientProfileResponseDTO>> addPatientProfile(
				@RequestPart("data") @Valid CreatePatientProfileRequestDTO request,
				@RequestPart(value = "image", required = false) MultipartFile image
		) throws Exception {
			CreatePatientProfileResponseDTO data = patientProfileService.addPatientProfile(request, image);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseConfig<>(
					Instant.now(),
					"Patient profile created successfully",
					data,
					HttpStatus.CREATED.value()
			));
		}
		
		@Operation(
				summary = "Fetch all patient profiles",
				description = "Returns a paginated and sorted list of all non-deleted patient profiles.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Patient profiles retrieved successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping
		public ResponseEntity<ApiResponseConfig<Page<CreatePatientProfileResponseDTO>>> fetchPatientProfiles(
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreatePatientProfileResponseDTO> data = patientProfileService.fetchPatientProfiles(buildPageable(page, size, sortBy, direction));
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Patient profiles retrieved successfully",
					data,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Fetch a patient profile by ID",
				description = "Returns the full details of a single patient profile.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Patient profile found"),
						@ApiResponse(responseCode = "404", description = "Patient profile not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<CreatePatientProfileResponseDTO>> fetchPatientProfile(
				@Parameter(description = "Patient profile ID") @PathVariable String id
		) throws  Exception {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Patient profile retrieved successfully",
					patientProfileService.fetchPatientProfile(id),
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Update a patient profile",
				description = "Updates an existing patient profile. Optionally replaces the profile image — old image is deleted from Cloudinary if a new one is provided.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Patient profile updated successfully"),
						@ApiResponse(responseCode = "400", description = "Validation error or duplicate entry"),
						@ApiResponse(responseCode = "404", description = "Patient profile not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreatePatientProfileResponseDTO>> updatePatientProfile(
				@Parameter(description = "Patient profile ID") @PathVariable String id,
				@RequestPart("data") @Valid CreatePatientProfileRequestDTO request,
				@RequestPart(value = "image", required = false) MultipartFile image
		) throws Exception {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Patient profile updated successfully",
					patientProfileService.updatePatientProfile(id, request, image),
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Soft-delete a patient profile",
				description = "Marks a patient profile as deleted without removing it from the database.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Patient profile deleted successfully"),
						@ApiResponse(responseCode = "404", description = "Patient profile not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@DeleteMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<Void>> deletePatientProfile(
				@Parameter(description = "Patient profile ID") @PathVariable String id
		) throws Exception {
			patientProfileService.deletePatientProfile(id);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Patient profile deleted successfully",
					null,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Count all patient profiles",
				description = "Returns the total number of active (non-deleted) patient profiles.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Count returned successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/count")
		public ResponseEntity<ApiResponseConfig<Map<String, Long>>> countPatientProfiles() {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Patient profile count retrieved successfully",
					Map.of("count", patientProfileService.countPatientProfiles()),
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Search patient profiles",
				description = "Search and filter patient profiles by keyword, gender, blood group, marital status, doctor, active/deceased status, and date range. Supports sorting and pagination.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Search results returned"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/search")
		public ResponseEntity<ApiResponseConfig<Page<CreatePatientProfileResponseDTO>>> searchPatientProfiles(
				@Parameter(description = "Keyword (matches name, email, phone, patient number, national ID)")
				@RequestParam(required = false) String keyword,
				
				@Parameter(description = "Filter by gender")
				@RequestParam(required = false) GenderType gender,
				
				@Parameter(description = "Filter by blood group")
				@RequestParam(required = false) BloodGroup bloodGroup,
				
				@Parameter(description = "Filter by marital status")
				@RequestParam(required = false) MaritalStatus maritalStatus,
				
				@Parameter(description = "Filter by primary doctor ID")
				@RequestParam(required = false) String primaryDoctorId,
				
				@Parameter(description = "Filter by active status")
				@RequestParam(required = false) Boolean active,
				
				@Parameter(description = "Filter by deceased status")
				@RequestParam(required = false) Boolean deceased,
				
				@Parameter(description = "Filter profiles created from this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
				
				@Parameter(description = "Filter profiles created up to this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
				
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreatePatientProfileResponseDTO> data = patientProfileService.searchPatientProfiles(
					keyword, gender, bloodGroup, maritalStatus,
					primaryDoctorId, active, deceased, from, to,
					buildPageable(page, size, sortBy, direction)
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