package com.medicalSolutionsInc.dto.medicalRecordDTO;

import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import com.medicalSolutionsInc.enumerations.visitType.VisitType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(name = "MedicalRecordResponse", description = "Medical record details returned in API responses")
public record MedicalRecordResponseDTO(
		
		@Schema(description = "Unique MongoDB ID", example = "664f1b2e3a4b5c6d7e8f9a0b")
		String id,
		
		@Schema(description = "Unique record number", example = "MR-2026-00123")
		String recordNumber,
		
		@Schema(description = "Type of medical record", example = "CONSULTATION")
		MedicalRecordType type,
		
		@Schema(description = "Status of the medical record", example = "OPEN")
		MedicalRecordStatus status,
		
		@Schema(description = "Title of the medical record", example = "Annual Blood Work")
		String title,
		
		@Schema(description = "Detailed description", example = "Routine checkup and blood panel")
		String description,
		
		@Schema(description = "Patient ID reference", example = "664f1b2e3a4b5c6d7e8f9a0b")
		String patientId,
		
		@Schema(description = "Full name of the patient", example = "John Doe")
		String patientName,
		
		@Schema(description = "Patient date of birth")
		Instant patientDateOfBirth,
		
		@Schema(description = "Patient blood group", example = "O_POSITIVE")
		BloodGroup bloodGroup,
		
		@Schema(description = "Patient gender", example = "MALE")
		String gender,
		
		@Schema(description = "Attending doctor ID", example = "664f1b2e3a4b5c6d7e8f9a0c")
		String attendingDoctorId,
		
		@Schema(description = "Full name of the attending doctor", example = "Dr. Jane Smith")
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
		
		@Schema(description = "Chief complaint / reason for visit", example = "Chest pain")
		String chiefComplaint,
		
		@Schema(description = "List of diagnoses", example = "[\"Hypertension\"]")
		List<String> diagnosis,
		
		@Schema(description = "List of symptoms", example = "[\"Fever\", \"Cough\"]")
		List<String> symptoms,
		
		@Schema(description = "List of prescriptions")
		List<PrescriptionDTO> prescriptions,
		
		@Schema(description = "List of lab results")
		List<LabResultDTO> labResults,
		
		@Schema(description = "Vital signs recorded during visit")
		VitalSignsDTO vitalSigns,
		
		@Schema(description = "Known allergies", example = "[\"Penicillin\"]")
		List<String> allergies,
		
		@Schema(description = "Attachments such as scans and reports")
		List<AttachmentDTO> attachments,
		
		@Schema(description = "Doctor notes")
		String notes,
		
		@Schema(description = "Whether the record is confidential", example = "false")
		boolean confidential,
		
		@Schema(description = "Whether the record is archived", example = "false")
		boolean archived,
		
		@Schema(description = "Timestamp when the record was created")
		Instant createdAt,
		
		@Schema(description = "Timestamp when the record was last updated")
		Instant updatedAt


) {}