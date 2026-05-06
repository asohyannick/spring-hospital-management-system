package com.medicalSolutionsInc.dto.paymentDTO;

import com.medicalSolutionsInc.enumerations.paymentType.PaymentType;
import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentRequestDTO(
		
		@NotNull(message = "Payment type is required")
		PaymentType paymentType,
		
		@NotBlank(message = "Currency is required")
		@Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code e.g. USD")
		String currency,
		
		@NotNull(message = "Amount is required")
		@DecimalMin(value = "0.01", message = "Amount must be greater than zero")
		BigDecimal amount,
		
		@NotNull(message = "Amount paid is required")
		@DecimalMin(value = "0.00", message = "Amount paid cannot be negative")
		BigDecimal amountPaid,
		
		@NotNull(message = "Amount due is required")
		@DecimalMin(value = "0.00", message = "Amount due cannot be negative")
		BigDecimal amountDue,
		
		@DecimalMin(value = "0.00", message = "Discount amount cannot be negative")
		BigDecimal discountAmount,
		
		@DecimalMin(value = "0.00", message = "Tax amount cannot be negative")
		BigDecimal taxAmount,
		
		@NotBlank(message = "Patient ID is required")
		String patientId,
		
		@NotBlank(message = "Patient name is required")
		String patientName,
		
		@NotBlank(message = "Patient email is required")
		@Email(message = "Invalid patient email format")
		String patientEmail,
		
		String patientPhone,
		
		String referenceId,
		
		ReferenceType referenceType,
		
		String description,
		
		String invoiceNumber,
		
		Instant dueDate,
		
		boolean partialPayment,
		
		boolean insuranceClaim,
		
		String insuranceClaimId,
		
		String insuranceProvider,
		
		@DecimalMin(value = "0.00", message = "Insurance covered amount cannot be negative")
		BigDecimal insuranceCoveredAmount,
		
		PaymentMethodDetailsDTO paymentMethodDetails
) {
public record PaymentMethodDetailsDTO(
		String cardBrand,
		String cardLastFour,
		String cardHolderName,
		String billingAddress,
		String walletType,
		String bankName,
		String accountLastFour
) {}
}