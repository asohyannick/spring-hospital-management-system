package com.medicalSolutionsInc.dto.patientProfileDTO;

import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(name = "CreatePatientProfileResponse", description = "Patient profile details returned in API responses")
public record CreatePatientProfileResponseDTO(
		
		@Schema(description = "Unique MongoDB ID", example = "664f1b2e3a4b5c6d7e8f9a0b")
		String id,
		
		@Schema(description = "Unique patient number", example = "PAT-2026-00123")
		String patientNumber,
		
		@Schema(description = "URL of the patient's profile image", example = "https://cdn.example.com/patient.jpg")
		String imageUrl,
		
		@Schema(description = "Patient's first name", example = "John")
		String firstName,
		
		@Schema(description = "Patient's last name", example = "Doe")
		String lastName,
		
		@Schema(description = "Patient's middle name", example = "James")
		String middleName,
		
		@Schema(description = "Patient's date of birth")
		Instant dateOfBirth,
		
		@Schema(description = "Patient's gender", example = "MALE")
		GenderType gender,
		
		@Schema(description = "Patient's blood group", example = "O_POSITIVE")
		BloodGroup bloodGroup,
		
		@Schema(description = "Patient's marital status", example = "SINGLE")
		MaritalStatus maritalStatus,
		
		@Schema(description = "Patient's nationality", example = "American")
		String nationality,
		
		@Schema(description = "Government-issued national ID", example = "A1234567")
		String nationalId,
		
		@Schema(description = "Patient's occupation", example = "Software Engineer")
		String occupation,
		
		@Schema(description = "Patient's email address", example = "john.doe@example.com")
		String email,
		
		@Schema(description = "Primary phone number", example = "+1234567890")
		String phoneNumber,
		
		@Schema(description = "Alternate phone number", example = "+0987654321")
		String alternatePhone,
		
		@Schema(description = "Patient's residential address")
		AddressDTO address,
		
		@Schema(description = "Emergency contact details")
		EmergencyContactDTO emergencyContact,
		
		@Schema(description = "Known allergies", example = "[\"Penicillin\"]")
		List<String> allergies,
		
		@Schema(description = "Chronic conditions", example = "[\"Diabetes\"]")
		List<String> chronicConditions,
		
		@Schema(description = "Current medications", example = "[\"Metformin 500mg\"]")
		List<String> currentMedications,
		
		@Schema(description = "Past surgeries", example = "[\"Appendectomy 2018\"]")
		List<String> pastSurgeries,
		
		@Schema(description = "Family medical history", example = "[\"Heart Disease\"]")
		List<String> familyMedicalHistory,
		
		@Schema(description = "Primary doctor ID reference", example = "664f1b2e3a4b5c6d7e8f9a0b")
		String primaryDoctorId,
		
		@Schema(description = "Primary doctor full name", example = "Dr. Jane Smith")
		String primaryDoctorName,
		
		@Schema(description = "Health insurance details")
		InsuranceDTO insurance,
		
		@Schema(description = "Whether the patient profile is active", example = "true")
		boolean active,
		
		@Schema(description = "Whether the patient is deceased", example = "false")
		boolean deceased,
		
		@Schema(description = "Timestamp when the patient passed away")
		Instant deceasedAt,
		
		@Schema(description = "Timestamp when the profile was created")
		Instant createdAt,
		
		@Schema(description = "Timestamp when the profile was last updated")
		Instant updatedAt

) { }