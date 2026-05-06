package com.medicalSolutionsInc.dto.paymentDTO;

import com.medicalSolutionsInc.enumerations.refundStatus.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record RefundResponseDTO(
		String refundId,
		BigDecimal amount,
		String reason,
		RefundStatus status,
		Instant initiatedAt,
		Instant processedAt
) {}