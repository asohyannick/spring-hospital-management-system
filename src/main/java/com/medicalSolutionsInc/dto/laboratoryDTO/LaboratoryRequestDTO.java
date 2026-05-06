package com.medicalSolutionsInc.dto.laboratoryDTO;

import com.medicalSolutionsInc.enumerations.laboratoryStatus.LaboratoryStatus;
import com.medicalSolutionsInc.enumerations.laboratoryType.LaboratoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(name = "LaboratoryRequest", description = "Payload for creating or updating a laboratory")
public record LaboratoryRequestDTO(
		
		@Schema(description = "Name of the laboratory", example = "City Diagnostic Lab")
		@NotBlank(message = "Laboratory name is required")
		@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
		String name,
		
		@Schema(description = "Official registration number", example = "LAB-REG-2026-001")
		@NotBlank(message = "Registration number is required")
		String registrationNumber,
		
		@Schema(description = "Accreditation number e.g ISO 15189", example = "ISO-15189-2024-XYZ")
		String accreditationNumber,
		
		@Schema(description = "Type of laboratory", example = "PATHOLOGY")
		@NotNull(message = "Laboratory type is required")
		LaboratoryType type,
		
		@Schema(description = "Operational status of the laboratory", example = "ACTIVE")
		LaboratoryStatus status,
		
		@Schema(description = "Official email address", example = "lab@citydiagnostic.com")
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		String email,
		
		@Schema(description = "Primary phone number", example = "+1234567890")
		@NotBlank(message = "Phone number is required")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
		String phone,
		
		@Schema(description = "Alternate phone number", example = "+0987654321")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid alternate phone number format")
		String alternatePhone,
		
		@Schema(description = "Fax number", example = "+1234567891")
		String fax,
		
		@Schema(description = "Laboratory website URL", example = "https://citydiagnosticlab.com")
		@Pattern(
				regexp = "^(https?://)?([\\w.-]+)\\.([a-z]{2,6})([/\\w .-]*)*/?$",
				message = "Invalid website URL format"
		)
		String website,
		
		@Schema(description = "Physical address of the laboratory")
		@NotNull(message = "Address is required")
		@Valid
		AddressDTO address,
		
		@Schema(description = "Brief description of the laboratory", example = "A full-service diagnostic laboratory")
		@Size(max = 500, message = "Description must not exceed 500 characters")
		String description,
		
		@Schema(description = "URL of the laboratory image", example = "https://cdn.example.com/lab.jpg")
		String imageUrl,
		
		@Schema(description = "Opening hours", example = "Mon-Fri 08:00-18:00")
		String openingHours,
		
		@Schema(description = "List of tests offered", example = "[\"CBC\", \"MRI\", \"PCR\"]")
		List<String> testsOffered,
		
		@Schema(description = "Days the laboratory operates", example = "[\"MONDAY\", \"TUESDAY\"]")
		List<String> operatingDays,
		
		@Schema(description = "Name of the head pathologist", example = "Dr. Jane Doe")
		String headPathologist,
		
		@Schema(description = "Name of the primary contact person", example = "John Smith")
		String contactPerson,
		
		@Schema(description = "Whether the laboratory accepts walk-in patients", example = "true")
		boolean acceptsWalkIns,
		
		@Schema(description = "Whether the laboratory accepts online bookings", example = "false")
		boolean acceptsOnlineBooking


) { }