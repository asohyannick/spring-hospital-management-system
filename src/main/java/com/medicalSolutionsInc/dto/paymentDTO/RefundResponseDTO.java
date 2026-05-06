package com.medicalSolutionsInc.dto.paymentDTO;

import com.medicalSolutionsInc.enumerations.refundStatus.RefundStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema (description = "Details of a single refund entry")
public record RefundResponseDTO(
		
		@Schema(description = "Stripe refund ID")
		String refundId,
		
		@Schema(description = "Refunded amount")
		BigDecimal amount,
		
		@Schema(description = "Reason provided for the refund")
		String reason,
		
		@Schema(description = "Current status of the refund")
		RefundStatus status,
		
		@Schema(description = "When the refund was initiated")
		Instant initiatedAt,
		
		@Schema(description = "When the refund was processed by the gateway")
		Instant processedAt
) {}