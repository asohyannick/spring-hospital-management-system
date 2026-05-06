package com.medicalSolutionsInc.dto.patientProfileDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema (name = "EmergencyContact", description = "Emergency contact details")
public record EmergencyContactDTO(
		
		@Schema(description = "Full name of emergency contact", example = "Jane Doe")
		@NotBlank (message = "Emergency contact full name is required")
		String fullName,
		
		@Schema(description = "Relationship to patient", example = "Spouse")
		@NotBlank(message = "Relationship is required")
		String relationship,
		
		@Schema(description = "Emergency contact phone number", example = "+1234567890")
		@NotBlank(message = "Emergency contact phone number is required")
		@Pattern (regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
		String phoneNumber,
		
		@Schema(description = "Alternate phone number", example = "+0987654321")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid alternate phone number format")
		String alternatePhone,
		
		@Schema(description = "Emergency contact email", example = "jane.doe@example.com")
		@Email (message = "Invalid email format")
		String email,
		
		@Schema(description = "Emergency contact address")
		@Valid
		AddressDTO address
) {}