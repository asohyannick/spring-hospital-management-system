package com.medicalSolutionsInc.dto.paymentDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payment method details returned to the client")
public record PaymentMethodDetailsResponseDTO(
		@Schema(description = "Card brand", example = "Visa")          String cardBrand,
		@Schema(description = "Card last four digits", example = "4242") String cardLastFour,
		@Schema(description = "Card holder name")                        String cardHolderName,
		@Schema(description = "Billing address")                         String billingAddress,
		@Schema(description = "Wallet type")                             String walletType,
		@Schema(description = "Bank name")                               String bankName,
		@Schema(description = "Bank account last four")                  String accountLastFour
) {}