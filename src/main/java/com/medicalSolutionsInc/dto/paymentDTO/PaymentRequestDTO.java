package com.medicalSolutionsInc.dto.paymentDTO;

import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import com.medicalSolutionsInc.enumerations.paymentType.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Request payload for processing a payment")
public record PaymentRequestDTO(
		
		@Schema(description = "Invoice number tied to this payment", example = "INV-2024-00123")
		@NotBlank(message = "Invoice number is required")
		String invoiceNumber,
		
		@Schema(description = "Payment type / gateway", example = "STRIPE")
		PaymentType paymentType,
		
		@Schema(description = "ISO 4217 currency code", example = "USD")
		@Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
		String currency,
		
		@Schema(description = "Total amount for the transaction", example = "150.00")
		@NotNull(message = "Amount is required")
		@DecimalMin(value = "0.01", message = "Amount must be greater than zero")
		BigDecimal amount,
		
		@Schema(description = "Discount applied to the total", example = "10.00")
		BigDecimal discountAmount,
		
		@Schema(description = "Tax amount included", example = "5.00")
		BigDecimal taxAmount,
		
		@Schema(description = "Patient ID", example = "pat_abc123")
		@NotBlank(message = "Patient ID is required")
		String patientId,
		
		@Schema(description = "Patient full name", example = "John Doe")
		@NotBlank(message = "Patient name is required")
		String patientName,
		
		@Schema(description = "Patient email address", example = "john.doe@example.com")
		@Email(message = "Invalid email format")
		String patientEmail,
		
		@Schema(description = "Patient phone number", example = "+1-555-000-1234")
		String patientPhone,
		
		@Schema(description = "ID of the referenced entity (e.g. prescription ID)")
		String referenceId,
		
		@Schema(description = "Type of the referenced entity", example = "PRESCRIPTION")
		String referenceType,
		
		@Schema(description = "Human-readable description of the payment", example = "Payment for prescription #RX-789")
		String description,
		
		@Schema(description = "Stripe PaymentMethod ID (pm_xxx) obtained from the frontend")
		@NotBlank(message = "Stripe payment method ID is required")
		String stripePaymentMethodId,
		
		@Schema(description = "Insurance claim ID if applicable")
		String insuranceClaimId,
		
		@Schema(description = "Insurance provider name")
		String insuranceProvider,
		
		@Schema(description = "Amount covered by insurance")
		BigDecimal insuranceCoveredAmount,
		
		@Schema(description = "Payment due date")
		Instant dueDate,
		
		@Schema(description = "Whether this is a partial payment")
		boolean partialPayment,
		
		@Schema(description = "Whether this involves an insurance claim")
		boolean insuranceClaim,
		
		@Schema(description = "Card / wallet payment method details")
		PaymentMethodDetailsDTO paymentMethodDetails
) {}