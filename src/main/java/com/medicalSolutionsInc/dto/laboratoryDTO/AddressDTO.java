package com.medicalSolutionsInc.dto.laboratoryDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema (name = "Address", description = "Physical address details")
public record AddressDTO(
		
		@Schema(description = "Street address", example = "123 Medical Drive")
		@NotBlank (message = "Street is required")
		String street,
		
		@Schema(description = "City", example = "New York")
		@NotBlank(message = "City is required")
		String city,
		
		@Schema(description = "State or province", example = "NY")
		@NotBlank(message = "State is required")
		String state,
		
		@Schema(description = "Country", example = "United States")
		@NotBlank(message = "Country is required")
		String country,
		
		@Schema(description = "Zip or postal code", example = "10001")
		@NotBlank(message = "Zip code is required")
		String zipCode,
		
		@Schema(description = "Latitude coordinate", example = "40.7128")
		double latitude,
		
		@Schema(description = "Longitude coordinate", example = "-74.0060")
		double longitude
) {}