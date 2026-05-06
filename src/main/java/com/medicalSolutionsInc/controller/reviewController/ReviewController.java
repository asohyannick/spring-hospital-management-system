package com.medicalSolutionsInc.controller.reviewController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.reviewDTO.CreateReviewRequestDTO;
import com.medicalSolutionsInc.dto.reviewDTO.CreateReviewResponseDTO;
import com.medicalSolutionsInc.enumerations.reviewStatus.ReviewStatus;
import com.medicalSolutionsInc.enumerations.reviewTargetType.ReviewTargetType;
import com.medicalSolutionsInc.service.review.ReviewService;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/${api.version}/review")
@RequiredArgsConstructor
@Tag(name = "Review Management Endpoints", description = "Endpoints for creating, retrieving, searching, and managing reviews")
public class ReviewController {

		private final ReviewService reviewService;
		
		@Operation(
				summary = "Create a new review",
				description = "Creates a new review. Accepts multipart/form-data with optional attachment image uploads.",
				responses = {
						@ApiResponse(responseCode = "201", description = "Review created successfully",
								content = @Content(schema = @Schema(implementation = CreateReviewResponseDTO.class))),
						@ApiResponse(responseCode = "400", description = "Validation error"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreateReviewResponseDTO>> addReview(
				@RequestPart("data") @Valid CreateReviewRequestDTO request,
				@RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
		) throws Exception {
			CreateReviewResponseDTO data = reviewService.addReview(request, attachments);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseConfig<>(
					Instant.now(),
					"Review created successfully",
					data,
					HttpStatus.CREATED.value()
			));
		}
		
		@Operation(
				summary = "Fetch all reviews",
				description = "Returns a paginated and sorted list of all non-deleted reviews.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping
		public ResponseEntity<ApiResponseConfig<Page<CreateReviewResponseDTO>>> fetchReviews(
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreateReviewResponseDTO> data = reviewService.fetchReviews(buildPageable(page, size, sortBy, direction));
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic ())
					       .body ( new ApiResponseConfig<>(
					Instant.now(),
					"Reviews retrieved successfully",
					data,
					HttpStatus.OK.value()
			) );
		}
		
		@Operation(
				summary = "Fetch a review by ID",
				description = "Returns the full details of a single review.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Review found"),
						@ApiResponse(responseCode = "404", description = "Review not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<CreateReviewResponseDTO>> fetchReview(
				@Parameter(description = "Review ID") @PathVariable String id
		) throws Exception {
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic ())
					       .body ( new ApiResponseConfig<>(
					Instant.now(),
					"Review retrieved successfully",
					reviewService.fetchReview(id),
					HttpStatus.OK.value()
			));
		}
		
		// ─── Update Review ─────────────────────────────────────────────────────────
		
		@Operation(
				summary = "Update a review",
				description = "Updates an existing review. Old attachments are deleted from Cloudinary and replaced with the new ones if provided.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Review updated successfully"),
						@ApiResponse(responseCode = "400", description = "Validation error"),
						@ApiResponse(responseCode = "404", description = "Review not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponseConfig<CreateReviewResponseDTO>> updateReview(
				@Parameter(description = "Review ID") @PathVariable String id,
				@RequestPart("data") @Valid CreateReviewRequestDTO request,
				@RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
		) throws Exception {
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Review updated successfully",
					reviewService.updateReview(id, request, attachments),
					HttpStatus.OK.value()
			));
		}
		
		// ─── Delete Review ─────────────────────────────────────────────────────────
		
		@Operation(
				summary = "Soft-delete a review",
				description = "Marks a review as deleted without removing it from the database.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Review deleted successfully"),
						@ApiResponse(responseCode = "404", description = "Review not found"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@DeleteMapping("/{id}")
		public ResponseEntity<ApiResponseConfig<Void>> deleteReview(
				@Parameter(description = "Review ID") @PathVariable String id
		) throws Exception {
			reviewService.deleteReview(id);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Review deleted successfully",
					null,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Count all reviews",
				description = "Returns the total number of active (non-deleted) reviews.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Count returned successfully"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/count")
		public ResponseEntity<ApiResponseConfig<Map<String, Long>>> countReviews() {
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
							       Instant.now(),
							       "Review count retrieved successfully",
							       Map.of("count", reviewService.countReviews()),
							       HttpStatus.OK.value()
					       ) );
		}
		
		// ─── Search Reviews ────────────────────────────────────────────────────────
		
		@Operation(
				summary = "Search reviews",
				description = "Search and filter reviews by keyword, target type, target ID, patient, status, rating range, verified visit, featured, and date range. Supports sorting and pagination.",
				responses = {
						@ApiResponse(responseCode = "200", description = "Search results returned"),
						@ApiResponse(responseCode = "403", description = "Access denied")
				}
		)
		@GetMapping("/search")
		public ResponseEntity<ApiResponseConfig<Page<CreateReviewResponseDTO>>> searchReviews(
				@Parameter(description = "Keyword (matches title, comment, patient name, target name, tags)")
				@RequestParam(required = false) String keyword,
				
				@Parameter(description = "Filter by target type")
				@RequestParam(required = false) ReviewTargetType targetType,
				
				@Parameter(description = "Filter by target ID")
				@RequestParam(required = false) String targetId,
				
				@Parameter(description = "Filter by patient ID")
				@RequestParam(required = false) String patientId,
				
				@Parameter(description = "Filter by review status")
				@RequestParam(required = false) ReviewStatus status,
				
				@Parameter(description = "Minimum rating (1-5)")
				@RequestParam(required = false) Integer minRating,
				
				@Parameter(description = "Maximum rating (1-5)")
				@RequestParam(required = false) Integer maxRating,
				
				@Parameter(description = "Filter by verified visit")
				@RequestParam(required = false) Boolean verifiedVisit,
				
				@Parameter(description = "Filter by featured status")
				@RequestParam(required = false) Boolean featured,
				
				@Parameter(description = "Filter reviews created from this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
				
				@Parameter(description = "Filter reviews created up to this date (ISO-8601)")
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
				
				@RequestParam(defaultValue = "0")         int page,
				@RequestParam(defaultValue = "10")        int size,
				@RequestParam(defaultValue = "createdAt") String sortBy,
				@RequestParam(defaultValue = "desc")      String direction
		) {
			Page<CreateReviewResponseDTO> data = reviewService.searchReviews(
					keyword, targetType, targetId, patientId,
					status, minRating, maxRating, verifiedVisit,
					featured, from, to, buildPageable(page, size, sortBy, direction)
			);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
							       Instant.now(),
							       "Search completed successfully",
							       data,
							       HttpStatus.OK.value()
					       ) );
		}
		
		private Pageable buildPageable(int page, int size, String sortBy, String direction) {
			Sort sort = direction.equalsIgnoreCase("asc")
					            ? Sort.by(sortBy).ascending()
					            : Sort.by(sortBy).descending();
			return PageRequest.of(page, size, sort);
		}
}