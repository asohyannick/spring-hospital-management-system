package com.medicalSolutionsInc.dto.bookingDTO;

import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Full booking details returned after creation or retrieval")
public record BookingResponseDTO(
		
		@Schema(description = "Unique booking ID", example = "64f1a2b3c4d5e6f7a8b9c0d1")
		String id,
		
		@Schema(description = "Profile image URL", example = "https://cdn.example.com/img.png")
		String imageURL,
		
		@Schema(description = "Patient first name", example = "John")
		String firstName,
		
		@Schema(description = "Patient last name", example = "Doe")
		String lastName,
		
		@Schema(description = "Patient age", example = "30")
		Integer age,
		
		@Schema(description = "Patient email address", example = "john.doe@example.com")
		String email,
		
		@Schema(description = "Patient phone number", example = "+1234567890")
		String phone,
		
		@Schema(description = "Street address", example = "123 Main St")
		String address,
		
		@Schema(description = "City", example = "New York")
		String city,
		
		@Schema(description = "State or province", example = "NY")
		String state,
		
		@Schema(description = "Zip or postal code", example = "10001")
		String zipCode,
		
		@Schema(description = "Country", example = "United States")
		String country,
		
		@Schema(description = "Patient profession", example = "Engineer")
		String profession,
		
		@Schema(description = "Marital status", example = "SINGLE")
		MaritalStatus maritalStatus,
		
		@Schema(description = "Date of birth", example = "1994-06-15")
		LocalDate birthDate,
		
		@Schema(description = "Employment status", example = "EMPLOYED")
		EmploymentStatus employmentStatus,
		
		@Schema(description = "Gender of the patient", example = "MALE")
		String gender,
		
		@Schema(description = "Emergency contact name", example = "Jane Doe")
		String emergencyContactName,
		
		@Schema(description = "Emergency contact phone", example = "+1987654321")
		String emergencyContactPhone,
		
		@Schema(description = "Insurance provider", example = "AXA Insurance")
		String insuranceProvider,
		
		@Schema(description = "Insurance number", example = "INS-123456789")
		String insuranceNumber,
		
		@Schema(
				description = "Check-in date and time (ISO 8601 format)",
				example = "2026-05-01T14:00:00Z",
				requiredMode = Schema.RequiredMode.REQUIRED
		)
		Instant checkInTime,
		
		@Schema(
				description = "Check-out date and time (must be after check-in time)",
				example = "2026-05-05T11:00:00Z",
				requiredMode = Schema.RequiredMode.REQUIRED
		)
		Instant checkOutTime,
		
		@Schema(description = "Additional notes", example = "Patient has a history of migraines")
		String notes,
		
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
		
		@Schema(description = "Current booking status", example = "PENDING")
		BookingStatus bookingStatus,
		
		@Schema(description = "Timestamp when booking was created")
		Instant createdAt,
		
		@Schema(description = "Timestamp when booking was last updated")
		Instant updatedAt

) {}