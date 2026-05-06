package com.medicalSolutionsInc.dto.paymentDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Schema(description = "Request payload for issuing a refund")
public record RefundRequestDTO(
		
		@Schema(description = "Amount to refund; omit or set to full amount for a complete refund", example = "50.00")
		@DecimalMin(value = "0.01", message = "Refund amount must be greater than zero")
		BigDecimal amount,
		
		@Schema(description = "Reason for the refund", example = "Patient requested cancellation")
		@NotBlank(message = "Refund reason is required")
		String reason
) {}


