package com.medicalSolutionsInc.controller.laboratoryController;

import com.medicalSolutionsInc.dto.laboratoryDTO.LaboratoryRequestDTO;
import com.medicalSolutionsInc.dto.laboratoryDTO.LaboratoryResponseDTO;
import com.medicalSolutionsInc.service.laboratory.LaboratoryService;
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
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/${api.version}/lab")
@RequiredArgsConstructor
@Tag ( name = "Laboratory Management Endpoints", description = "")
public class LaboratoryController {
		private final LaboratoryService laboratoryService;
		
		@Operation (
				summary = "Add a new laboratory",
				description = "Creates a new laboratory record. Accepts multipart/form-data with an optional image upload.",
				responses = {
						@ApiResponse ( responseCode = "201", description = "Laboratory created successfully",
								content = @Content ( schema = @Schema ( implementation = LaboratoryResponseDTO.class ) ) ) ,
						@ApiResponse ( responseCode = "400", description = "Validation error or duplicate entry" ) ,
						@ApiResponse ( responseCode = "403", description = "Access denied" )
				}
		)
		@PostMapping ( consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
		public ResponseEntity < LaboratoryResponseDTO > addLaboratory (
				@RequestPart ( "data" ) @Valid LaboratoryRequestDTO request ,
				@RequestPart ( value = "image", required = false ) MultipartFile image
		) throws Exception {
			LaboratoryResponseDTO response = laboratoryService.addLaboratory ( request , image );
			return ResponseEntity.status ( HttpStatus.CREATED ).body ( response );
		}
		
		@Operation (
				summary = "Fetch all laboratories",
				description = "Returns a paginated list of all non-deleted laboratories.",
				responses = {
						@ApiResponse ( responseCode = "200", description = "List retrieved successfully" ) ,
						@ApiResponse ( responseCode = "403", description = "Access denied" )
				}
		)
		@GetMapping
		public ResponseEntity < Page < LaboratoryResponseDTO > > fetchLaboratories (
				@Parameter ( description = "Page number (0-indexed)" ) @RequestParam ( defaultValue = "0" ) int page ,
				@Parameter ( description = "Page size" ) @RequestParam ( defaultValue = "10" ) int size ,
				@Parameter ( description = "Sort field" ) @RequestParam ( defaultValue = "createdAt" ) String sortBy ,
				@Parameter ( description = "Sort direction: asc or desc" ) @RequestParam ( defaultValue = "desc" ) String direction
		) {
			Sort sort = direction.equalsIgnoreCase ( "asc" ) ? Sort.by ( sortBy ).ascending ( ) : Sort.by ( sortBy ).descending ( );
			Pageable pageable = PageRequest.of ( page , size , sort );
			return ResponseEntity.ok ()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( laboratoryService.fetchLaboratories ( pageable ));
		}
		
		@Operation(
				summary = "Update a laboratory",
				description = "Updates an existing laboratory's details. Optionally replaces the image — old image is deleted from Cloudinary if a new one is provided.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Laboratory updated successfully",
								content = @Content(schema = @Schema(implementation = LaboratoryResponseDTO.class))),
						@ApiResponse(responseCode = "400", description = "Validation error or duplicate entry"),
						@ApiResponse(responseCode = "404", description = "Laboratory not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<LaboratoryResponseDTO> updateLaboratory(
				@Parameter(description = "Laboratory ID") @PathVariable String id,
				@RequestPart("data")
				@Valid LaboratoryRequestDTO request,
				@RequestPart(value = "image", required = false) MultipartFile image
		) throws Exception {
			return ResponseEntity.ok(laboratoryService.updateLaboratory(id, request, image));
		}
		
		@Operation (
				summary = "Fetch a laboratory by ID",
				description = "Returns the details of a single laboratory by its ID.",
				responses = {
						@ApiResponse ( responseCode = "200", description = "Laboratory found" ) ,
						@ApiResponse ( responseCode = "404", description = "Laboratory not found" ) ,
						@ApiResponse ( responseCode = "403", description = "Access denied" )
				}
		)
		@GetMapping ( "/{id}" )
		@ResponseStatus( HttpStatus.OK )
		public ResponseEntity < LaboratoryResponseDTO > fetchLaboratory (
				@Parameter ( description = "Laboratory ID" ) @PathVariable String id
		) throws  Exception {
			return ResponseEntity.ok ()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( laboratoryService.fetchLaboratory ( id ) );
		}
		
		@Operation (
				summary = "Soft-delete a laboratory",
				description = "Marks a laboratory as deleted without permanently removing it from the database.",
				responses = {
						@ApiResponse ( responseCode = "204", description = "Laboratory deleted successfully" ) ,
						@ApiResponse ( responseCode = "404", description = "Laboratory not found" ) ,
						@ApiResponse ( responseCode = "403", description = "Access denied" )
				}
		)
		@DeleteMapping ( "/{id}" )
		@ResponseStatus( HttpStatus.NO_CONTENT )
		public ResponseEntity < Void > deleteLaboratory (
				@Parameter ( description = "Laboratory ID" )
				@PathVariable String id
		) throws  Exception {
			laboratoryService.deleteLaboratory ( id );
			return ResponseEntity.noContent ( ).build ( );
		}
		
		@Operation (
				summary = "Search laboratories",
				description = "Search laboratories by name, email, or registration number.",
				responses = {
						@ApiResponse ( responseCode = "200", description = "Search results returned" ) ,
						@ApiResponse ( responseCode = "403", description = "Access denied" )
				}
		)
		@GetMapping ( "/search" )
		public ResponseEntity < Page < LaboratoryResponseDTO > > searchLaboratories (
				@Parameter ( description = "Search keyword" ) @RequestParam String keyword ,
				@RequestParam ( defaultValue = "0" ) int page ,
				@RequestParam ( defaultValue = "10" ) int size ,
				@RequestParam ( defaultValue = "createdAt" ) String sortBy ,
				@RequestParam ( defaultValue = "desc" ) String direction
		) {
			Sort sort = direction.equalsIgnoreCase ( "asc" ) ? Sort.by ( sortBy ).ascending ( ) : Sort.by ( sortBy ).descending ( );
			Pageable pageable = PageRequest.of ( page , size , sort );
			return ResponseEntity.ok (  )
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( laboratoryService.searchLaboratories ( keyword , pageable ) );
		}
		
		@Operation (
				summary = "Count all laboratories",
				description = "Returns the total number of active (non-deleted) laboratories.",
				responses = {
						@ApiResponse ( responseCode = "200", description = "Count returned successfully" ) ,
						@ApiResponse ( responseCode = "403", description = "Access denied" )
				}
		)
		@GetMapping ( "/count" )
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity < Map < String, Long > > countLaboratories ( ) {
			return ResponseEntity.ok (  )
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( Map.of ( "count" , laboratoryService.countLaboratories ( ) ) );
		}
}
