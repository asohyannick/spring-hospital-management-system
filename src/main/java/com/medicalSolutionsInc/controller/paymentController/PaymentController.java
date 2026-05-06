package com.medicalSolutionsInc.controller.paymentController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.paymentDTO.PaymentRequestDTO;
import com.medicalSolutionsInc.dto.paymentDTO.PaymentResponseDTO;
import com.medicalSolutionsInc.dto.paymentDTO.RefundRequestDTO;
import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import com.medicalSolutionsInc.service.payment.PaymentService;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/${api.version}/payment")
@RequiredArgsConstructor
@Tag(
		name        = "Payment Management Endpoints",
		description = "Endpoints for processing drug payments, viewing payment histories, issuing refunds, and managing payment records."
)
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

		private final PaymentService paymentService;
		
		
		@Operation(
				summary     = "Process a payment",
				description = """
		                    Charges the patient via Stripe for drugs or services linked to an invoice.
		                    A Stripe `PaymentMethod` ID (`pm_xxx`) obtained from the frontend (Stripe.js / Elements)
		                    must be included in the request body.
		                    
		                    **Roles allowed**: ADMIN, SUPER_ADMIN, DOCTOR, NURSE, PATIENT
		                    """
		)
		@ApiResponses({
				@ApiResponse(responseCode = "201", description = "Payment processed successfully",
						content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
				@ApiResponse(responseCode = "402", description = "Stripe charge failed – see message for details",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "409", description = "Payment for this invoice already exists"),
				@ApiResponse(responseCode = "422", description = "Validation error in request body"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Access denied")
		})
		@PostMapping("/checkout")
		@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DOCTOR','NURSE','PATIENT')")
		public ResponseEntity<ApiResponseConfig<PaymentResponseDTO>> processPayment(
				@Valid @RequestBody PaymentRequestDTO requestDTO
		) {
			PaymentResponseDTO response = paymentService.processPayment(requestDTO);
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(new ApiResponseConfig<>(
							       Instant.now(),
							       "Payment processed successfully",
							       response,
							       HttpStatus.CREATED.value()
					       ));
		}
		
		@Operation(
				summary     = "Fetch paginated payment histories",
				description = """
		                    Returns a paginated list of payment records.
		                    Optionally filter by `patientId` and / or `status`.
		                    
		                    **Roles allowed**: ADMIN, SUPER_ADMIN, DOCTOR, NURSE
		                    """
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Payment histories retrieved successfully"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Access denied")
		})
		@GetMapping
		@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DOCTOR','NURSE')")
		public ResponseEntity<ApiResponseConfig<Page<PaymentResponseDTO>>> fetchPaymentHistories(
				
				@Parameter(description = "Filter results to a specific patient ID")
				@RequestParam(required = false) String patientId,
				
				@Parameter(description = "Filter by payment status", schema = @Schema(implementation = PaymentStatus.class))
				@RequestParam(required = false) PaymentStatus status,
				
				@ParameterObject
				@PageableDefault(size = 20, sort = "createdAt") Pageable pageable
		) {
			Page<PaymentResponseDTO> page = paymentService.fetchPaymentHistories(patientId, status, pageable);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
					Instant.now(),
					"Payment histories retrieved successfully",
					page,
					HttpStatus.OK.value()
			) );
		}
		
		@Operation(
				summary     = "Fetch a single payment record",
				description = """
		                    Returns the full details of a payment by its internal document ID.
		                    
		                    **Roles allowed**: ADMIN, SUPER_ADMIN, DOCTOR, NURSE, PATIENT
		                    """
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Payment record retrieved successfully"),
				@ApiResponse(responseCode = "404", description = "Payment not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Access denied")
		})
		@GetMapping("/{id}")
		@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DOCTOR','NURSE','PATIENT')")
		public ResponseEntity<ApiResponseConfig<PaymentResponseDTO>> fetchPaymentHistory(
				
				@Parameter(description = "Internal payment document ID", required = true, example = "664f1b2c3d4e5f6a7b8c9d0e")
				@PathVariable String id
		) throws Exception  {
			PaymentResponseDTO response = paymentService.fetchPaymentHistory(id);
			return ResponseEntity.ok()
					       .cacheControl ( CacheControl.maxAge ( 5, TimeUnit.MINUTES ).cachePublic () )
					       .body ( new ApiResponseConfig<>(
							Instant.now(),
							"Payment record retrieved successfully",
							response,
							HttpStatus.OK.value()
			) );
		}
		
		@Operation(
				summary     = "Soft-delete a payment record",
				description = """
		                    Marks a payment record as deleted (`deletedAt` timestamp is set).
		                    Only **FAILED** or **CANCELLED** payments may be removed to preserve financial audit trails.
		                    COMPLETED, REFUNDED, and PARTIALLY_REFUNDED records cannot be deleted.
		                    
		                    **Roles allowed**: ADMIN, SUPER_ADMIN
		                    """
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Payment record deleted successfully"),
				@ApiResponse(responseCode = "400", description = "Payment status does not allow deletion"),
				@ApiResponse(responseCode = "404", description = "Payment not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Access denied")
		})
		@DeleteMapping("/{id}")
		@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
		public ResponseEntity<ApiResponseConfig<Void>> deletePaymentHistory(
				
				@Parameter(description = "Internal payment document ID", required = true)
				@PathVariable String id
		) throws Exception {
			paymentService.deletePaymentHistory(id);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Payment record deleted successfully",
					null,
					HttpStatus.OK.value()
			));
		}
		
		@Operation(
				summary     = "Refund a payment (full or partial)",
				description = """
		                    Issues a refund via the Stripe Refunds API against an existing COMPLETED or
		                    PARTIALLY_REFUNDED payment.
		                    
		                    - Omit `amount` to refund the full remaining balance.
		                    - Provide `amount` for a partial refund.
		                    - Stripe's accepted refund reasons are automatically mapped from the `reason` field.
		                    
		                    **Roles allowed**: ADMIN, SUPER_ADMIN
		                    """
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Refund processed successfully",
						content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
				@ApiResponse(responseCode = "400", description = "Invalid refund amount or payment not eligible for refund"),
				@ApiResponse(responseCode = "404", description = "Payment not found"),
				@ApiResponse(responseCode = "500", description = "Stripe refund API error"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Access denied")
		})
		@PostMapping("/{id}/refund")
		@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
		public ResponseEntity<ApiResponseConfig<PaymentResponseDTO>> refundPayment(
				
				@Parameter(description = "Internal payment document ID to refund", required = true)
				@PathVariable String id,
				
				@Valid @RequestBody RefundRequestDTO refundRequestDTO
		) throws Exception {
			PaymentResponseDTO response = paymentService.refundPayment(id, refundRequestDTO);
			return ResponseEntity.ok(new ApiResponseConfig<>(
					Instant.now(),
					"Refund processed successfully",
					response,
					HttpStatus.OK.value()
			));
		}
}