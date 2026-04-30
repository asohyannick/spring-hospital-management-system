package com.medicalSolutionsInc.dto.bookingDTO;

import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Request payload for updating an existing booking")
public record UpdateBookingResponseDTO(
		
		@Schema(description = "Profile image URL", example = "https://cdn.example.com/img.png")
		@Pattern(regexp = "^(https?://).+", message = "imageURL must be a valid URL")
		String imageURL,
		
		@Schema(description = "Patient first name", example = "John")
		@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
		String firstName,
		
		@Schema(description = "Patient last name", example = "Doe")
		@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
		String lastName,
		
		@Schema(description = "Patient age", example = "30")
		@Min(value = 0, message = "Age must be a positive number")
		@Max(value = 150, message = "Age must be a realistic value")
		Integer age,
		
		@Schema(description = "Patient email address", example = "john.doe@example.com")
		@Email(message = "Email must be valid")
		String email,
		
		@Schema(description = "Patient phone number", example = "+1234567890")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid")
		String phone,
		
		@Schema(description = "Street address", example = "123 Main St")
		@Size(max = 100, message = "Address must not exceed 100 characters")
		String address,
		
		@Schema(description = "City", example = "New York")
		@Size(max = 50, message = "City must not exceed 50 characters")
		String city,
		
		@Schema(description = "State or province", example = "NY")
		@Size(max = 50, message = "State must not exceed 50 characters")
		String state,
		
		@Schema(description = "Zip or postal code", example = "10001")
		@Pattern(regexp = "^[A-Z0-9\\- ]{3,10}$", message = "Zip code must be valid")
		String zipCode,
		
		@Schema(description = "Country", example = "United States")
		@Size(max = 60, message = "Country must not exceed 60 characters")
		String country,
		
		@Schema(description = "Patient profession", example = "Engineer")
		@Size(max = 100, message = "Profession must not exceed 100 characters")
		String profession,
		
		@Schema(description = "Marital status", example = "SINGLE")
		MaritalStatus maritalStatus,
		
		@Schema(description = "Date of birth", example = "1994-06-15")
		@Past(message = "Birth date must be in the past")
		LocalDate birthDate,
		
		@Schema(description = "Employment status", example = "EMPLOYED")
		EmploymentStatus employmentStatus,
		
		@Schema(description = "List of hobbies")
		List<String> hobbies,
		
		@Schema(description = "List of known allergies")
		List<String> allergies,
		
		@Schema(description = "List of known diseases")
		List<String> diseases,
		
		@Schema(description = "List of current symptoms")
		List<String> symptoms,
		
		@Schema(description = "List of previous medications")
		List<String> previousMedications,
		
		@Schema(description = "Academic levels or qualifications")
		List<String> academicLevel,
		
		@Schema(description = "Gender of the patient", example = "MALE")
		String gender,
		
		@Schema(description = "Emergency contact name", example = "Jane Doe")
		@Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
		String emergencyContactName,
		
		@Schema(description = "Emergency contact phone", example = "+1987654321")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Emergency contact phone must be valid")
		String emergencyContactPhone,
		
		@Schema(description = "Insurance provider", example = "AXA Insurance")
		@Size(max = 100, message = "Insurance provider must not exceed 100 characters")
		String insuranceProvider,
		
		@Schema(description = "Additional notes", example = "Patient has a history of migraines")
		@Size(max = 500, message = "Notes must not exceed 500 characters")
		String notes,
		
		@Schema(description = "Updated booking status", example = "CONFIRMED")
		BookingStatus bookingStatus

) {
public UpdateBookingResponseDTO {
	hobbies = hobbies != null ? hobbies : new ArrayList<>();
	allergies = allergies != null ? allergies : new ArrayList<>();
	diseases = diseases != null ? diseases : new ArrayList<>();
	symptoms = symptoms != null ? symptoms : new ArrayList<>();
	previousMedications = previousMedications != null ? previousMedications : new ArrayList<>();
	academicLevel = academicLevel != null ? academicLevel : new ArrayList<>();
}
}