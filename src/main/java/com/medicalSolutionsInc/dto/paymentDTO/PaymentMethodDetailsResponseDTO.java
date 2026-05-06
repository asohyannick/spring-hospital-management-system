package com.medicalSolutionsInc.dto.paymentDTO;

public record PaymentMethodDetailsResponseDTO(
		String cardBrand,
		String cardLastFour,
		String cardHolderName,
		String billingAddress,
		String walletType,
		String bankName,
		String accountLastFour
) {}