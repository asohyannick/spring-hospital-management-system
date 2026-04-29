package com.medicalSolutionsInc.dto.userDTO;
import com.medicalSolutionsInc.enumerations.user.UserRole;
import jakarta.validation.constraints.*;
public record RegistrationRequestDTO(

        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @NotBlank(message = "First name is required")
        String firstName,

        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
		
        @NotNull(message = "User role is required")
        UserRole role
) { }
