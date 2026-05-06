package com.medicalSolutionsInc.controller.medicalRecordController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.medicalRecordDTO.MedicalRecordRequestDTO;
import com.medicalSolutionsInc.dto.medicalRecordDTO.MedicalRecordResponseDTO;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import com.medicalSolutionsInc.service.medicalRecord.MedicalRecordService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/${api.version}/medical-record")
@RequiredArgsConstructor
@Tag(name = "Medical Record Management Endpoints", description = "Endpoints for creating, retrieving, searching, and managing medical records")
public class MedicalRecordController {

		private final MedicalRecordService medicalRecordService;
		
		@Operation(
				summary = "Create a new medical record",
				description = "Creates a new medical record for a patient.",
				responses = {
						@ApiResponse(responseCode = "201", description = "Medical record created successfully",
								content = @Content(schema = @Schema(implementation = MedicalRecordResponseDTO.class))),
						@ApiResponse(responseCode = "400", description = "Validation error"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PostMapping
		public ResponseEntity<ApiResponseConfig<MedicalRecordResponseDTO>> addMedicalRecord(
				@RequestBody @Valid MedicalRecordRequestDTO request
		) {
			MedicalRecordResponseDTO data = medicalRecordService.addMedicalRecord(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseConfig<>(
					Instant.now(),
					"Medical record created successfully",
					data,
					HttpStatus.CREATED.value()
			));
		}
		
		@Operation(
				summary = "Fetch all medical records",
				description = "Returns a paginated and sorted list of all non-deleted medical records.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Records retrieved successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<ApiResponseConfig<Page<MedicalRecordResponseDTO>>> fetchMedicalRecords(
				@RequestParam(defaultValue = "0")      int page,
				@RequestParam(defaultValue = "10")     int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")   String direction
		) {
			Pageable pageable = buildPageable(page, size, sortBy, direction);
			Page<MedicalRecordResponseDTO> data = medicalRecordService.fetchMedicalRecords(pageable);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
					Instant.now(),
					"Medical records retrieved successfully",
					data,
					HttpStatus.OK.value()
			) );
		}
		
		@Operation(
				summary = "Fetch a medical record by ID",
				description = "Returns the full details of a single medical record.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Record found"),
						@ApiResponse(responseCode = "404", description = "Record not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<MedicalRecordResponseDTO>> fetchMedicalRecord(
				@Parameter(description = "Medical record ID")
				@PathVariable String id
		) throws Exception {
			MedicalRecordResponseDTO data = medicalRecordService.fetchMedicalRecord(id);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
							       Instant.now(),
							       "Medical record retrieved successfully",
							       data,
							       HttpStatus.OK.value()
					       ) );
		}
		
		@Operation(
				summary = "Update a medical record",
				description = "Updates an existing medical record by ID.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Record updated successfully"),
						@ApiResponse(responseCode = "400", description = "Validation error"),
						@ApiResponse(responseCode = "404", description = "Record not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PutMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<MedicalRecordResponseDTO>> updateMedicalRecord(
				@Parameter(description = "Medical record ID") @PathVariable String id,
				@RequestBody @Valid MedicalRecordRequestDTO request
		) throws Exception {
			MedicalRecordResponseDTO data = medicalRecordService.updateMedicalRecord(id, request);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Medical record updated successfully",
					data,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Soft-delete a medical record",
				description = "Marks a medical record as deleted without removing it from the database.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Record deleted successfully"),
						@ApiResponse(responseCode = "404", description = "Record not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@DeleteMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<Void>> deleteMedicalRecord(
				@Parameter(description = "Medical record ID") @PathVariable String id
		) throws Exception {
			medicalRecordService.deleteMedicalRecord(id);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Medical record deleted successfully",
					null,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Count all medical records",
				description = "Returns the total number of active (non-deleted) medical records.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Count returned successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/count")
		public ResponseEntity<ApiResponseConfig<Map<String, Long>>> countMedicalRecords() {
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
							       Instant.now(),
							       "Medical record count retrieved successfully",
							       Map.of("count", medicalRecordService.countMedicalRecords()),
							       HttpStatus.OK.value()
					       ) );
		}
		
		@Operation(
				summary = "Search medical records",
				description = "Search and filter medical records by keyword, status, type, patient, doctor, facility, and date range. Supports sorting and pagination.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Search results returned"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/search")
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<ApiResponseConfig<Page<MedicalRecordResponseDTO>>> searchMedicalRecords(
				@Parameter(description = "Search keyword (matches title, patient name, chief complaint, record number)")
				@RequestParam(required = false) String keyword,
				
				@Parameter(description = "Filter by record status")
				@RequestParam(required = false) MedicalRecordStatus status,
				
				@Parameter(description = "Filter by record type")
				@RequestParam(required = false) MedicalRecordType type,
				
				@Parameter(description = "Filter by patient ID")
				@RequestParam(required = false) String patientId,
				
				@Parameter(description = "Filter by attending doctor ID")
				@RequestParam(required = false) String attendingDoctorId,
				
				@Parameter(description = "Filter by facility ID")
				@RequestParam(required = false) String facilityId,
				
				@Parameter(description = "Filter records created from this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
				
				@Parameter(description = "Filter records created up to this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
				
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Pageable pageable = buildPageable(page, size, sortBy, direction);
			Page<MedicalRecordResponseDTO> data = medicalRecordService.searchMedicalRecords(
					keyword, status, type, patientId, attendingDoctorId, facilityId, from, to, pageable
			);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body (
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