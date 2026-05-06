package com.medicalSolutionsInc.dto.patientProfileDTO;

import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;

@Schema(name = "CreatePatientProfileRequest", description = "Payload for creating or updating a patient profile")
public record CreatePatientProfileRequestDTO(
		
		@Schema(description = "URL of the patient's profile image", example = "https://cdn.example.com/patient.jpg")
		String imageUrl,
		
		@Schema(description = "Patient's first name", example = "John")
		@NotBlank(message = "First name is required")
		@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
		String firstName,
		
		@Schema(description = "Patient's last name", example = "Doe")
		@NotBlank(message = "Last name is required")
		@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
		String lastName,
		
		@Schema(description = "Patient's middle name", example = "James")
		String middleName,
		
		@Schema(description = "Patient's date of birth")
		@NotNull(message = "Date of birth is required")
		Instant dateOfBirth,
		
		@Schema(description = "Patient's gender", example = "MALE")
		@NotNull(message = "Gender is required")
		GenderType gender,
		
		@Schema(description = "Patient's blood group", example = "O_POSITIVE")
		BloodGroup bloodGroup,
		
		@Schema(description = "Patient's marital status", example = "SINGLE")
		MaritalStatus maritalStatus,
		
		@Schema(description = "Patient's nationality", example = "American")
		String nationality,
		
		@Schema(description = "Government-issued national ID number", example = "A1234567")
		String nationalId,
		
		@Schema(description = "Patient's occupation", example = "Software Engineer")
		String occupation,
		
		@Schema(description = "Patient's email address", example = "john.doe@example.com")
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		String email,
		
		@Schema(description = "Primary phone number", example = "+1234567890")
		@NotBlank(message = "Phone number is required")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
		String phoneNumber,
		
		@Schema(description = "Alternate phone number", example = "+0987654321")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid alternate phone number format")
		String alternatePhone,
		
		@Schema(description = "Patient's residential address")
		@NotNull(message = "Address is required")
		@Valid
		AddressDTO address,
		
		@Schema(description = "Emergency contact details")
		@Valid
		EmergencyContactDTO emergencyContact,
		
		@Schema(description = "Known allergies", example = "[\"Penicillin\", \"Peanuts\"]")
		List<String> allergies,
		
		@Schema(description = "Chronic conditions", example = "[\"Diabetes\", \"Hypertension\"]")
		List<String> chronicConditions,
		
		@Schema(description = "Current medications", example = "[\"Metformin 500mg\", \"Lisinopril 10mg\"]")
		List<String> currentMedications,
		
		@Schema(description = "Past surgeries", example = "[\"Appendectomy 2018\"]")
		List<String> pastSurgeries,
		
		@Schema(description = "Family medical history", example = "[\"Heart Disease\", \"Diabetes\"]")
		List<String> familyMedicalHistory,
		
		@Schema(description = "Primary doctor ID reference", example = "664f1b2e3a4b5c6d7e8f9a0b")
		String primaryDoctorId,
		
		@Schema(description = "Primary doctor full name", example = "Dr. Jane Smith")
		String primaryDoctorName,
		
		@Schema(description = "Health insurance details")
		@Valid
		InsuranceDTO insurance

) {
@Schema(name = "Address", description = "Residential address details")
record AddressDTO(
		
		@Schema(description = "Street address", example = "123 Main Street")
		@NotBlank(message = "Street is required")
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

@Schema(name = "EmergencyContact", description = "Emergency contact details")
record EmergencyContactDTO(
		
		@Schema(description = "Full name of emergency contact", example = "Jane Doe")
		@NotBlank(message = "Emergency contact full name is required")
		String fullName,
		
		@Schema(description = "Relationship to patient", example = "Spouse")
		@NotBlank(message = "Relationship is required")
		String relationship,
		
		@Schema(description = "Emergency contact phone number", example = "+1234567890")
		@NotBlank(message = "Emergency contact phone number is required")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
		String phoneNumber,
		
		@Schema(description = "Alternate phone number", example = "+0987654321")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid alternate phone number format")
		String alternatePhone,
		
		@Schema(description = "Emergency contact email", example = "jane.doe@example.com")
		@Email(message = "Invalid email format")
		String email,
		
		@Schema(description = "Emergency contact address")
		@Valid
		AddressDTO address
) {}

@Schema(name = "Insurance", description = "Health insurance details")
record InsuranceDTO(
		
		@Schema(description = "Insurance provider name", example = "AXA Health")
		@NotBlank(message = "Insurance provider name is required")
		String providerName,
		
		@Schema(description = "Policy number", example = "POL-2026-00123")
		@NotBlank(message = "Policy number is required")
		String policyNumber,
		
		@Schema(description = "Group number", example = "GRP-001")
		String groupNumber,
		
		@Schema(description = "Coverage type", example = "Full")
		String coverageType,
		
		@Schema(description = "Insurance expiry date")
		Instant expiryDate,
		
		@Schema(description = "Whether the insurance is currently active", example = "true")
		boolean active
) {}
}