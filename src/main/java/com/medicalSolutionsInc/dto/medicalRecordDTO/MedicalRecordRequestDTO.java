package com.medicalSolutionsInc.dto.medicalRecordDTO;
import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import com.medicalSolutionsInc.enumerations.visitType.VisitType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;

@Schema(name = "MedicalRecordRequest", description = "Payload for creating or updating a medical record")
public record MedicalRecordRequestDTO(
		
		@Schema(description = "Type of medical record", example = "CONSULTATION")
		@NotNull(message = "Record type is required")
		MedicalRecordType type,
		
		@Schema(description = "Status of the medical record", example = "OPEN")
		MedicalRecordStatus status,
		
		@Schema(description = "Title of the medical record", example = "Annual Blood Work")
		@NotBlank(message = "Title is required")
		@Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters")
		String title,
		
		@Schema(description = "Detailed description of the record", example = "Routine checkup and blood panel")
		@Size(max = 1000, message = "Description must not exceed 1000 characters")
		String description,
		
		@Schema(description = "Patient ID reference", example = "664f1b2e3a4b5c6d7e8f9a0b")
		@NotBlank(message = "Patient ID is required")
		String patientId,
		
		@Schema(description = "Full name of the patient", example = "John Doe")
		@NotBlank(message = "Patient name is required")
		String patientName,
		
		@Schema(description = "Patient date of birth")
		Instant patientDateOfBirth,
		
		@Schema(description = "Patient blood group", example = "O_POSITIVE")
		BloodGroup bloodGroup,
		
		@Schema(description = "Patient gender", example = "MALE")
		String gender,
		
		@Schema(description = "Attending doctor ID reference", example = "664f1b2e3a4b5c6d7e8f9a0c")
		@NotBlank(message = "Attending doctor ID is required")
		String attendingDoctorId,
		
		@Schema(description = "Full name of the attending doctor", example = "Dr. Jane Smith")
		@NotBlank(message = "Attending doctor name is required")
		String attendingDoctorName,
		
		@Schema(description = "Hospital department", example = "Cardiology")
		String department,
		
		@Schema(description = "Facility ID reference", example = "664f1b2e3a4b5c6d7e8f9a0d")
		String facilityId,
		
		@Schema(description = "Check-in timestamp")
		Instant checkIn,
		
		@Schema(description = "Check-out timestamp")
		Instant checkOut,
		
		@Schema(description = "Next appointment timestamp")
		Instant nextAppointment,
		
		@Schema(description = "Type of visit", example = "OUTPATIENT")
		VisitType visitType,
		
		@Schema(description = "Ward name or number", example = "ICU Ward A")
		String ward,
		
		@Schema(description = "Bed number", example = "BED-001")
		String bedNumber,
		
		@Schema(description = "Chief complaint / reason for visit", example = "Chest pain and shortness of breath")
		@NotBlank(message = "Chief complaint is required")
		String chiefComplaint,
		
		@Schema(description = "List of diagnoses", example = "[\"Hypertension\", \"Type 2 Diabetes\"]")
		List<String> diagnosis,
		
		@Schema(description = "List of symptoms", example = "[\"Fever\", \"Cough\"]")
		List<String> symptoms,
		
		@Schema(description = "List of prescriptions")
		@Valid
		List<PrescriptionDTO> prescriptions,
		
		@Schema(description = "List of lab results")
		@Valid
		List<LabResultDTO> labResults,
		
		@Schema(description = "Vital signs recorded during visit")
		@Valid
		VitalSignsDTO vitalSigns,
		
		@Schema(description = "Known allergies", example = "[\"Penicillin\", \"Peanuts\"]")
		List<String> allergies,
		
		@Schema(description = "Doctor notes", example = "Patient responded well to treatment")
		@Size(max = 2000, message = "Notes must not exceed 2000 characters")
		String notes,
		
		@Schema(description = "Whether the record is confidential", example = "false")
		boolean confidential


) { }