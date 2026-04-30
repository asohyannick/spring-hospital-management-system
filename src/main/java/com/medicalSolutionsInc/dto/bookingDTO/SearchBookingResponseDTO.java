package com.medicalSolutionsInc.dto.bookingDTO;

import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;

@Schema(description = "Lightweight booking summary returned in search results")
public record SearchBookingResponseDTO(
		
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
		
		@Schema(description = "City", example = "New York")
		String city,
		
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
		
		@Schema(description = "Additional notes", example = "Patient has a history of migraines")
		String notes,
		
		@Schema(description = "Current booking status", example = "PENDING")
		BookingStatus bookingStatus,
		
		@Schema(description = "Timestamp when booking was created")
		Instant createdAt

) {}