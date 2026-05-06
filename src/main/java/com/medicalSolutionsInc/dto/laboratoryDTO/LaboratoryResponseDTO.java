package com.medicalSolutionsInc.dto.laboratoryDTO;

import com.medicalSolutionsInc.enumerations.laboratoryStatus.LaboratoryStatus;
import com.medicalSolutionsInc.enumerations.laboratoryType.LaboratoryType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(name = "LaboratoryResponse", description = "Laboratory details returned in API responses")
public record LaboratoryResponseDTO(
		
		@Schema(description = "Unique MongoDB ID", example = "664f1b2e3a4b5c6d7e8f9a0b")
		String id,
		
		@Schema(description = "Name of the laboratory", example = "City Diagnostic Lab")
		String name,
		
		@Schema(description = "Official registration number", example = "LAB-REG-2026-001")
		String registrationNumber,
		
		@Schema(description = "Accreditation number", example = "ISO-15189-2024-XYZ")
		String accreditationNumber,
		
		@Schema(description = "Type of laboratory", example = "PATHOLOGY")
		LaboratoryType type,
		
		@Schema(description = "Operational status", example = "ACTIVE")
		LaboratoryStatus status,
		
		@Schema(description = "Official email address", example = "lab@citydiagnostic.com")
		String email,
		
		@Schema(description = "Primary phone number", example = "+1234567890")
		String phone,
		
		@Schema(description = "Alternate phone number", example = "+0987654321")
		String alternatePhone,
		
		@Schema(description = "Fax number", example = "+1234567891")
		String fax,
		
		@Schema(description = "Laboratory website URL", example = "https://citydiagnosticlab.com")
		String website,
		
		@Schema(description = "Physical address of the laboratory")
		AddressDTO address,
		
		@Schema(description = "Brief description", example = "A full-service diagnostic laboratory")
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
		boolean acceptsOnlineBooking,
		
		@Schema(description = "Whether the laboratory is verified", example = "false")
		boolean verified,
		
		@Schema(description = "Timestamp when the record was created")
		Instant createdAt,
		
		@Schema(description = "Timestamp when the record was last updated")
		Instant updatedAt


) {
@Schema(name = "Address", description = "Physical address details")
record AddressDTO(
		
		@Schema(description = "Street address", example = "123 Medical Drive")
		String street,
		
		@Schema(description = "City", example = "New York")
		String city,
		
		@Schema(description = "State or province", example = "NY")
		String state,
		
		@Schema(description = "Country", example = "United States")
		String country,
		
		@Schema(description = "Zip or postal code", example = "10001")
		String zipCode,
		
		@Schema(description = "Latitude coordinate", example = "40.7128")
		double latitude,
		
		@Schema(description = "Longitude coordinate", example = "-74.0060")
		double longitude
) {}
}