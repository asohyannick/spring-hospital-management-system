package com.medicalSolutionsInc.dto.bookingDTO;

import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Request payload for creating a new booking")
public record BookingRequestDTO(
		
		@Schema(description = "Profile image URL", example = "https://cdn.example.com/img.png")
		@Pattern(regexp = "^(https?://).+", message = "imageURL must be a valid URL")
		String imageURL,
		
		@Schema(description = "Patient first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotBlank(message = "First name is required")
		@Size(min = 2, max = 50)
		String firstName,
		
		@Schema(description = "Patient last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotBlank(message = "Last name is required")
		@Size(min = 2, max = 50)
		String lastName,
		
		@Schema(description = "Patient age", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull(message = "Age is required")
		@Min(0)
		@Max(150)
		Integer age,
		
		@Schema(description = "Patient email address", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotBlank(message = "Email is required")
		@Email
		String email,
		
		@Schema(description = "Patient phone number", example = "+1234567890")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid")
		String phone,
		
		@Schema(description = "Street address", example = "123 Main St")
		@Size(max = 100)
		String address,
		
		@Schema(description = "City", example = "New York")
		@Size(max = 50)
		String city,
		
		@Schema(description = "State or province", example = "NY")
		@Size(max = 50)
		String state,
		
		@Schema(description = "Zip or postal code", example = "10001")
		@Pattern(regexp = "^[A-Z0-9\\- ]{3,10}$")
		String zipCode,
		
		@Schema(description = "Country", example = "United States")
		@Size(max = 60)
		String country,
		
		@Schema(description = "Patient profession", example = "Engineer")
		@Size(max = 100)
		String profession,
		
		@Schema(description = "Marital status of the patient", example = "SINGLE")
		MaritalStatus maritalStatus,
		
		@Schema(description = "Date of birth", example = "1994-06-15")
		@Past
		LocalDate birthDate,
		
		@Schema(description = "Employment status", example = "EMPLOYED")
		EmploymentStatus employmentStatus,
		
		@Schema(description = "Gender of the patient", example = "MALE")
		String gender,
		
		@Schema(description = "Emergency contact name", example = "Jane Doe")
		@Size(max = 100)
		String emergencyContactName,
		
		@Schema(description = "Emergency contact phone", example = "+1987654321")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$")
		String emergencyContactPhone,
		
		@Schema(description = "Insurance provider", example = "AXA Insurance")
		@Size(max = 100)
		String insuranceProvider,
		
		@Schema(description = "Insurance number", example = "INS-123456789")
		@Size(max = 50)
		String insuranceNumber,
		
		@Schema(description = "Additional notes", example = "Patient has a history of migraines")
		@Size(max = 500)
		String notes,
		
		@Schema(description = "List of hobbies", example = "[\"Reading\", \"Swimming\"]")
		List<String> hobbies,
		
		@Schema(description = "List of known allergies", example = "[\"Penicillin\", \"Peanuts\"]")
		List<String> allergies,
		
		@Schema(description = "List of known diseases", example = "[\"Diabetes\", \"Hypertension\"]")
		List<String> diseases,
		
		@Schema(description = "List of current symptoms", example = "[\"Headache\", \"Fever\"]")
		List<String> symptoms,
		
		@Schema(description = "List of previous medications", example = "[\"Metformin\", \"Lisinopril\"]")
		List<String> previousMedications,
		
		@Schema(description = "Academic levels or qualifications", example = "[\"Bachelor's Degree\"]")
		List<String> academicLevel

) {

public BookingRequestDTO {
	hobbies = hobbies != null ? hobbies : new ArrayList<>();
	allergies = allergies != null ? allergies : new ArrayList<>();
	diseases = diseases != null ? diseases : new ArrayList<>();
	symptoms = symptoms != null ? symptoms : new ArrayList<>();
	previousMedications = previousMedications != null ? previousMedications : new ArrayList<>();
	academicLevel = academicLevel != null ? academicLevel : new ArrayList<>();
}
}