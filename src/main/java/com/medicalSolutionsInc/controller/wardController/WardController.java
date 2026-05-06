package com.medicalSolutionsInc.controller.wardController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.updateWardRequestDTO.UpdateWardRequestDTO;
import com.medicalSolutionsInc.dto.wardDTO.CreateWardRequestDTO;
import com.medicalSolutionsInc.dto.wardDTO.CreateWardResponseDTO;
import com.medicalSolutionsInc.service.ward.WardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${api.version}/ward")
@RequiredArgsConstructor
@Tag(name = "Ward and Bed Management Endpoints", description = "Endpoints for managing hospital wards, bed allocations, and visiting configurations")
@SecurityRequirement(name = "bearerAuth")
public class WardController {

		private final WardService wardService;
		
		@Operation(
				summary = "Add a new ward",
				description = "Creates a new hospital ward with bed configuration, staff assignments, and visiting hours. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "201", description = "Ward created successfully",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "Invalid request payload"),
				@ApiResponse(responseCode = "401", description = "Unauthorized — authentication required"),
				@ApiResponse(responseCode = "403", description = "Forbidden — insufficient permissions")
		})
		@PostMapping
		@ResponseStatus(HttpStatus.CREATED)
		public ApiResponseConfig<CreateWardResponseDTO> addWard(
				@Valid @RequestBody CreateWardRequestDTO request
		) {
			return wardService.addWard(request);
		}
		
		@Operation(
				summary = "Fetch all wards",
				description = "Retrieves a list of all active (non-deleted) wards. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Wards fetched successfully"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Forbidden")
		})
		@GetMapping
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<List<CreateWardResponseDTO>> fetchWards() throws Exception {
			return wardService.fetchWards();
		}
		
		@Operation(
				summary = "Count total active wards",
				description = "Returns the total count of active wards in the system. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Count fetched successfully"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Forbidden")
		})
		@GetMapping("/count")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<Long> countTotalWards() {
			return wardService.countTotalWards();
		}
		
		@Operation(
				summary = "Search wards with filtering, sorting, and pagination",
				description = """
		                    Search wards by keyword or specific filters. Supports full pagination and sorting.
		                    
		                    **Sortable fields:** `name`, `type`, `status`, `floor`, `building`, `totalBeds`, `createdAt`, `updatedAt`
		                    
		                    **Filterable fields:** `type`, `status`, `floor`, `building`, `isolationWard`, `privateWard`, `pediatric`, `acceptsEmergency`
		                    
		                    Requires ADMIN or SUPER_ADMIN role.
		                    """
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Search completed successfully"),
				@ApiResponse(responseCode = "400", description = "Invalid search parameters"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Forbidden")
		})
		@GetMapping("/search")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<Page<CreateWardResponseDTO>> searchWards(
				
				@Parameter(description = "Keyword to search across name, wardNumber, description, building, floor")
				@RequestParam(required = false) String keyword,
				
				@Parameter(description = "Filter by ward type (e.g., GENERAL, ICU, MATERNITY)")
				@RequestParam(required = false) String type,
				
				@Parameter(description = "Filter by ward status (e.g., ACTIVE, INACTIVE, UNDER_MAINTENANCE)")
				@RequestParam(required = false) String status,
				
				@Parameter(description = "Filter by floor location")
				@RequestParam(required = false) String floor,
				
				@Parameter(description = "Filter by building or block")
				@RequestParam(required = false) String building,
				
				@Parameter(description = "Filter isolation wards only")
				@RequestParam(required = false) Boolean isolationWard,
				
				@Parameter(description = "Filter private wards only")
				@RequestParam(required = false) Boolean privateWard,
				
				@Parameter(description = "Filter pediatric wards only")
				@RequestParam(required = false) Boolean pediatric,
				
				@Parameter(description = "Filter wards that accept emergencies")
				@RequestParam(required = false) Boolean acceptsEmergency,
				
				@Parameter(description = "Page number (0-indexed)", example = "0")
				@RequestParam(defaultValue = "0") int page,
				
				@Parameter(description = "Number of results per page", example = "10")
				@RequestParam(defaultValue = "10") int size,
				
				@Parameter(description = "Field to sort by", example = "createdAt")
				@RequestParam(defaultValue = "createdAt") String sortBy,
				
				@Parameter(description = "Sort direction: asc or desc", example = "desc")
				@RequestParam(defaultValue = "desc") String sortDirection
		) {
			return wardService.searchWards(
					keyword, type, status, floor, building,
					isolationWard, privateWard, pediatric, acceptsEmergency,
					page, size, sortBy, sortDirection
			);
		}
		
		@Operation(
				summary = "Fetch a single ward by ID",
				description = "Retrieves detailed information about a specific ward. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Ward fetched successfully"),
				@ApiResponse(responseCode = "404", description = "Ward not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Forbidden")
		})
		@GetMapping("/{wardId}")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<CreateWardResponseDTO> fetchWard(
				@Parameter(description = "The unique ID of the ward", required = true)
				@PathVariable String wardId
		) throws Exception {
			return wardService.fetchWard(wardId);
		}
		
		@Operation(
				summary = "Update a ward",
				description = "Updates an existing ward's details. Only provided fields are updated (partial update). Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Ward updated successfully"),
				@ApiResponse(responseCode = "400", description = "Invalid update payload"),
				@ApiResponse(responseCode = "404", description = "Ward not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Forbidden")
		})
		@PutMapping("/{wardId}")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<CreateWardResponseDTO> updateWard(
				@Parameter(description = "The unique ID of the ward to update", required = true)
				@PathVariable String wardId,
				@Valid @RequestBody UpdateWardRequestDTO request
		) throws Exception {
			return wardService.updateWard(wardId, request);
		}
		
		@Operation(
				summary = "Delete a ward (soft delete)",
				description = "Soft-deletes a ward by setting its deletedAt timestamp. The ward is no longer returned in active queries. Requires SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Ward deleted successfully"),
				@ApiResponse(responseCode = "404", description = "Ward not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Forbidden — SUPER_ADMIN only")
		})
		@DeleteMapping("/{wardId}")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<Void> deleteWard(
				@Parameter(description = "The unique ID of the ward to delete", required = true)
				@PathVariable String wardId
		) throws Exception{
			return wardService.deleteWard(wardId);
		}
}