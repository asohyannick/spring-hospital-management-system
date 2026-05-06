package com.medicalSolutionsInc.dto.paymentDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payment method details supplied by the client")
public record PaymentMethodDetailsDTO(
		
		@Schema(description = "Card brand (Visa, Mastercard …)", example = "Visa")
		String cardBrand,
		
		@Schema(description = "Last four digits of the card", example = "4242")
		String cardLastFour,
		
		@Schema(description = "Name on the card", example = "John Doe")
		String cardHolderName,
		
		@Schema(description = "Billing address string")
		String billingAddress,
		
		@Schema(description = "Wallet type if applicable (Apple Pay, Google Pay …)")
		String walletType,
		
		@Schema(description = "Bank name for ACH / bank transfer")
		String bankName,
		
		@Schema(description = "Last four digits of the bank account", example = "6789")
		String accountLastFour
) {}