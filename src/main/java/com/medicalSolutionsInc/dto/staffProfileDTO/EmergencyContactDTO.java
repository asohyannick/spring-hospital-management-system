package com.medicalSolutionsInc.dto.staffProfileDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmergencyContactDTO(
		@NotBlank String fullName,
		@NotBlank String relationship,
		@NotBlank String phoneNumber,
		String alternatePhone,
		@Email String email
) {}