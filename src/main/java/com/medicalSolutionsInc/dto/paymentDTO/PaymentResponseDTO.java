package com.medicalSolutionsInc.dto.paymentDTO;

import com.medicalSolutionsInc.enumerations.paymentStatus.PaymentStatus;
import com.medicalSolutionsInc.enumerations.paymentType.PaymentType;
import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import com.medicalSolutionsInc.enumerations.refundStatus.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PaymentResponseDTO(
		
		String id,
		String paymentNumber,
		String invoiceNumber,
		PaymentType paymentType,
		PaymentStatus status,
		String currency,
		BigDecimal amount,
		BigDecimal amountPaid,
		BigDecimal amountDue,
		BigDecimal discountAmount,
		BigDecimal taxAmount,
		BigDecimal refundedAmount,
		String patientId,
		String patientName,
		String patientEmail,
		String patientPhone,
		String referenceId,
		ReferenceType referenceType,
		String description,
		String transactionId,
		String gatewayReference,
		String gatewayResponseCode,
		String gatewayResponseMessage,
		String insuranceClaimId,
		String insuranceProvider,
		BigDecimal insuranceCoveredAmount,
		boolean partialPayment,
		boolean insuranceClaim,
		boolean receiptSent,
		Instant paidAt,
		Instant dueDate,
		Instant refundedAt,
		Instant createdAt,
		Instant updatedAt,
		Instant deletedAt,
		
		PaymentMethodDetailsResponseDTO paymentMethodDetails,
		List<RefundResponseDTO> refunds
) {
public record PaymentMethodDetailsResponseDTO(
		String cardBrand,
		String cardLastFour,
		String cardHolderName,
		String billingAddress,
		String walletType,
		String bankName,
		String accountLastFour
) {}

public record RefundResponseDTO(
		String refundId,
		BigDecimal amount,
		String reason,
		RefundStatus status,
		Instant initiatedAt,
		Instant processedAt
) {}
}