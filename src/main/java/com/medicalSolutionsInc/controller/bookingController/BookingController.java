package com.medicalSolutionsInc.controller.bookingController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.bookingDTO.BookingRequestDTO;
import com.medicalSolutionsInc.dto.bookingDTO.BookingResponseDTO;
import com.medicalSolutionsInc.dto.bookingDTO.SearchBookingResponseDTO;
import com.medicalSolutionsInc.dto.bookingDTO.UpdateBookingResponseDTO;
import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import com.medicalSolutionsInc.mappers.bookingMapper.BookingMapper;
import com.medicalSolutionsInc.service.booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/${api.version}/booking")
@RequiredArgsConstructor
@Tag(name = "Bookings Management Endpoints", description = "Endpoints for creating, retrieving, updating, deleting, and searching bookings")
public final class BookingController {

		private final BookingMapper bookingMapper;
		private final BookingService bookingService;
		
		@Operation(
				summary = "Create a new booking",
				description = "Creates a new booking record. Optionally accepts a profile image upload."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "201", description = "Booking created successfully",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "A booking with the given email already exists",
						content = @Content(schema = @Schema(example = "{\"error\": \"A booking with email x@x.com already exists\"}")))
		})
		@PostMapping(value = "/add-booking", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<ApiResponseConfig< BookingResponseDTO >> createBooking(
				@RequestPart("booking")
				@Parameter(description = "Booking details as JSON", required = true)
				BookingRequestDTO dto,
				
				@RequestPart(value = "image", required = false)
				@Parameter(description = "Optional profile image")
				MultipartFile image
		) throws Exception {
			BookingResponseDTO created = bookingService.createBooking(dto, image);
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(new ApiResponseConfig<>(
							       Instant.now(),
							       "Booking created successfully",
							       created,
							       HttpStatus.CREATED.value()
					       ));
		}
		

		@Operation(
				summary = "Fetch all bookings",
				description = "Returns a paginated, sorted list of all bookings."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "Invalid pagination or sort parameters",
						content = @Content(schema = @Schema(example = "{\"error\": \"Invalid sort field\"}")))
		})
		@GetMapping
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<ApiResponseConfig<Page<SearchBookingResponseDTO>>> fetchBookings(
				@RequestParam(defaultValue = "0")        @Parameter(description = "Page number (0-based)")       int page,
				@RequestParam(defaultValue = "10")       @Parameter(description = "Page size")                   int size,
				@RequestParam(defaultValue = "createdAt")@Parameter(description = "Field to sort by")            String sortBy,
				@RequestParam(defaultValue = "desc")     @Parameter(description = "Sort direction: asc or desc") String sortDir
		) {
			Page<SearchBookingResponseDTO> bookings = bookingService.fetchBookings(page, size, sortBy, sortDir);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
					Instant.now(),
					"Bookings retrieved successfully",
					bookings,
					HttpStatus.OK.value()
			) );
		}
		

		@Operation(
				summary = "Fetch a booking by ID",
				description = "Returns the full details of a single booking."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Booking retrieved successfully",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "Booking not found",
						content = @Content(schema = @Schema(example = "{\"error\": \"Booking not found with ID: abc\"}")))
		})
		@GetMapping("/{id}")
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<ApiResponseConfig<BookingResponseDTO>> fetchBooking(
				@PathVariable @Parameter(description = "Booking ID", required = true) String id
		) throws Exception {
			BookingResponseDTO booking = bookingService.fetchBooking(id);
			return ResponseEntity.ok().cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ) .cachePublic ()).body (
					new ApiResponseConfig<>(
							Instant.now(),
							"Booking retrieved successfully",
							booking,
							HttpStatus.OK.value()
					)
			);
		}
		
		@Operation(
				summary = "Update an existing booking",
				description = "Updates booking details. Optionally replaces the profile image."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Booking updated successfully",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "Booking not found",
						content = @Content(schema = @Schema(example = "{\"error\": \"Booking not found with ID: abc\"}")))
		})
		@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<ApiResponseConfig<BookingResponseDTO>> updateBooking(
				@PathVariable @Parameter(description = "Booking ID", required = true) String id,
				
				@RequestPart("booking")
				@Parameter(description = "Updated booking details as JSON", required = true)
				UpdateBookingResponseDTO dto,
				
				@RequestPart(value = "image", required = false)
				@Parameter(description = "Optional replacement profile image")
				MultipartFile image
		) throws Exception {
			BookingResponseDTO updated = bookingService.updateBooking(id, dto, image);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Booking updated successfully",
					updated,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Delete a booking",
				description = "Permanently deletes a booking and its associated profile image from Cloudinary."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Booking deleted successfully",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "Booking not found",
						content = @Content(schema = @Schema(example = "{\"error\": \"Booking not found with ID: abc\"}")))
		})
		@DeleteMapping("/{id}")
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public ResponseEntity<ApiResponseConfig<Void>> deleteBooking(
				@PathVariable @Parameter(description = "Booking ID", required = true) String id
		) throws Exception {
			bookingService.deleteBooking(id);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Booking deleted successfully",
					null,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary = "Search bookings with filters",
				description = "Advanced search with optional keyword, demographic, health, and date range filters. All parameters are optional."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Search results returned successfully",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "Invalid search parameters",
						content = @Content(schema = @Schema(example = "{\"error\": \"Invalid date range\"}")))
		})
		@GetMapping("/search")
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<ApiResponseConfig<Page< SearchBookingResponseDTO >>> searchBookings(
				@RequestParam(required = false) @Parameter(description = "Full-text keyword")                                                           String keyword,
				@RequestParam(required = false) @Parameter(description = "Filter by first name")                                                        String firstName,
				@RequestParam(required = false) @Parameter(description = "Filter by last name")                                                         String lastName,
				@RequestParam(required = false) @Parameter(description = "Filter by email")                                                             String email,
				@RequestParam(required = false) @Parameter(description = "Filter by phone")                                                             String phone,
				@RequestParam(required = false) @Parameter(description = "Filter by city")                                                              String city,
				@RequestParam(required = false) @Parameter(description = "Filter by state")                                                             String state,
				@RequestParam(required = false) @Parameter(description = "Filter by country")                                                           String country,
				@RequestParam(required = false) @Parameter(description = "Filter by zip code")                                                          String zipCode,
				@RequestParam(required = false) @Parameter(description = "Filter by profession")                                                        String profession,
				@RequestParam(required = false) @Parameter(description = "Filter by booking status") BookingStatus bookingStatus,
				@RequestParam(required = false) @Parameter(description = "Filter by marital status") MaritalStatus maritalStatus,
				@RequestParam(required = false) @Parameter(description = "Filter by employment status") EmploymentStatus employmentStatus,
				@RequestParam(required = false) @Parameter(description = "Minimum age filter")                                                          Integer minAge,
				@RequestParam(required = false) @Parameter(description = "Maximum age filter")                                                          Integer maxAge,
				@RequestParam(required = false) @Parameter(description = "Filter by allergy")                                                           String hasAllergy,
				@RequestParam(required = false) @Parameter(description = "Filter by disease")                                                           String hasDisease,
				@RequestParam(required = false) @Parameter(description = "Filter by symptom")                                                           String hasSymptom,
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Created from (ISO 8601)") Instant createdFrom,
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Created to (ISO 8601)")   Instant createdTo,
				@RequestParam(defaultValue = "0")         @Parameter(description = "Page number (0-based)")      int page,
				@RequestParam(defaultValue = "10")        @Parameter(description = "Page size")                  int size,
				@RequestParam(defaultValue = "createdAt") @Parameter(description = "Field to sort by")           String sortBy,
				@RequestParam(defaultValue = "desc")      @Parameter(description = "Sort direction: asc or desc") String sortDir
		) {
			Page<SearchBookingResponseDTO> results = bookingService.searchBookings(
					keyword, firstName, lastName, email, phone,
					city, state, country, zipCode, profession,
					bookingStatus, maritalStatus, employmentStatus,
					minAge, maxAge, hasAllergy, hasDisease, hasSymptom,
					createdFrom, createdTo,
					page, size, sortBy, sortDir
			);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
					Instant.now(),
					"Search completed successfully",
					results,
					HttpStatus.OK.value()
			) );
		}
		
		@Operation(
				summary = "Count total bookings",
				description = "Returns the total number of bookings in the system."
		)
		@ApiResponse(responseCode = "200", description = "Total count returned successfully",
				content = @Content(schema = @Schema(implementation = ApiResponseConfig.class)))
		@GetMapping("/count")
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<ApiResponseConfig<Long>> countTotalBookings() {
			long count = bookingService.countTotalBookings();
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body (
					new ApiResponseConfig<>(
							Instant.now(),
							"Total booking count retrieved successfully",
							count,
							HttpStatus.OK.value()
					)
			);
		}
}