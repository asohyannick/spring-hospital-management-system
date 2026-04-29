package com.medicalSolutionsInc.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public record CreatePasswordRequestDTO(
		
		@NotBlank(message = "Password is required")
		@Pattern(
				regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
				message = "Password must be 8-20 characters long, include uppercase, lowercase, number, and special character"
		)
		String password,
		
		@NotBlank(message = "Confirm Password is required")
		String confirmPassword
) {}