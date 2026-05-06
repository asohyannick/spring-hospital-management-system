package com.medicalSolutionsInc.dto.paymentDTO;

import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import com.medicalSolutionsInc.enumerations.paymentType.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Schema(description = "Payment response returned to the client")
public record PaymentResponseDTO(
		
		@Schema(description = "Internal payment document ID")
		String id,
		
		@Schema(description = "Human-readable payment number", example = "PAY-20240601-000042")
		String paymentNumber,
		
		@Schema(description = "Invoice number linked to this payment")
		String invoiceNumber,
		
		@Schema(description = "Payment gateway used")
		PaymentType paymentType,
		
		@Schema(description = "Current payment status")
		PaymentStatus status,
		
		@Schema(description = "ISO 4217 currency code", example = "USD")
		String currency,
		
		@Schema(description = "Original total amount")
		BigDecimal amount,
		
		@Schema(description = "Amount actually collected")
		BigDecimal amountPaid,
		
		@Schema(description = "Amount still due")
		BigDecimal amountDue,
		
		@Schema(description = "Discount applied")
		BigDecimal discountAmount,
		
		@Schema(description = "Tax included")
		BigDecimal taxAmount,
		
		@Schema(description = "Total amount refunded so far")
		BigDecimal refundedAmount,
		
		@Schema(description = "Patient identifier")
		String patientId,
		
		@Schema(description = "Patient full name")
		String patientName,
		
		@Schema(description = "Patient email")
		String patientEmail,
		
		@Schema(description = "Patient phone")
		String patientPhone,
		
		@Schema(description = "Referenced entity ID")
		String referenceId,
		
		@Schema(description = "Referenced entity type")
		String referenceType,
		
		@Schema(description = "Payment description")
		String description,
		
		@Schema(description = "Stripe / gateway transaction ID")
		String transactionId,
		
		@Schema(description = "Gateway-level reference")
		String gatewayReference,
		
		@Schema(description = "Gateway response code")
		String gatewayResponseCode,
		
		@Schema(description = "Gateway response message")
		String gatewayResponseMessage,
		
		@Schema(description = "Card / wallet details captured at payment time")
		PaymentMethodDetailsResponseDTO paymentMethodDetails,
		
		@Schema(description = "Insurance claim ID")
		String insuranceClaimId,
		
		@Schema(description = "Insurance provider name")
		String insuranceProvider,
		
		@Schema(description = "Amount covered by insurance")
		BigDecimal insuranceCoveredAmount,
		
		@Schema(description = "List of refunds issued against this payment")
		List<RefundResponseDTO> refunds,
		
		@Schema(description = "Timestamp when payment was completed")
		Instant paidAt,
		
		@Schema(description = "Payment due date")
		Instant dueDate,
		
		@Schema(description = "Timestamp when last refund was processed")
		Instant refundedAt,
		
		@Schema(description = "Whether this is a partial payment")
		boolean partialPayment,
		
		@Schema(description = "Whether this involves an insurance claim")
		boolean insuranceClaim,
		
		@Schema(description = "Whether a receipt has been sent")
		boolean receiptSent,
		
		@Schema(description = "Record creation timestamp")
		Instant createdAt,
		
		@Schema(description = "Record last-updated timestamp")
		Instant updatedAt
) {}