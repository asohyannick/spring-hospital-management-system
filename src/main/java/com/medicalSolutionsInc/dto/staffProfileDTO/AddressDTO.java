package com.medicalSolutionsInc.dto.staffProfileDTO;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
		String street,
		@NotBlank(message = "City is required") String city,
		String state,
		@NotBlank(message = "Country is required") String country,
		String zipCode,
		double latitude,
		double longitude
) {}