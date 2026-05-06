package com.medicalSolutionsInc.entity.medicalRecord;

import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.medicalRecordStatus.MedicalRecordStatus;
import com.medicalSolutionsInc.enumerations.medicalRecordType.MedicalRecordType;
import com.medicalSolutionsInc.enumerations.visitType.VisitType;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "medical_records")
public class MedicalRecord {
		
		@Id
		private String id;
		
		@Indexed(unique = true)
		@Field("record_number")
		private String recordNumber;
		
		@Field("type")
		private MedicalRecordType type;
		
		@Field("status")
		@Builder.Default
		private MedicalRecordStatus status = MedicalRecordStatus.OPEN;
		
		@Field("title")
		private String title;
		
		@Field("description")
		private String description;
		
		@Indexed
		@Field("patient_id")
		private String patientId;
		
		@Field("patient_name")
		private String patientName;
		
		@Field("patient_date_of_birth")
		private Instant patientDateOfBirth;
		
		@Field("patient_blood_group")
		private BloodGroup bloodGroup;
		
		@Field("patient_gender")
		private Gender gender;
		
		@Indexed
		@Field("attending_doctor_id")
		private String attendingDoctorId;
		
		@Field("attending_doctor_name")
		private String attendingDoctorName;
		
		@Field("department")
		private String department;
		
		@Field("facility_id")
		private String facilityId;
		
		@Field("check_in")
		private Instant checkIn;
		
		@Field("check_out")
		private Instant checkOut;
		
		@Field("next_appointment")
		private Instant nextAppointment;
		
		@Field("visit_type")
		private VisitType visitType;
		
		@Field("ward")
		private String ward;
		
		@Field("bed_number")
		private String bedNumber;
		
		@Field("chief_complaint")
		private String chiefComplaint;
		
		@Field("diagnosis")
		@Builder.Default
		private List<String> diagnosis = new ArrayList<>();
		
		@Field("symptoms")
		@Builder.Default
		private List<String> symptoms = new ArrayList<>();
		
		@Field("prescriptions")
		@Builder.Default
		private List<Prescription> prescriptions = new ArrayList<>();
		
		@Field("lab_results")
		@Builder.Default
		private List<LabResult> labResults = new ArrayList<>();
		
		@Field("vital_signs")
		private VitalSigns vitalSigns;
		
		@Field("allergies")
		@Builder.Default
		private List<String> allergies = new ArrayList<>();
		
		@Field("notes")
		private String notes;
		
		@Field("attachments")
		@Builder.Default
		private List<Attachment> attachments = new ArrayList<>();
		
		@Field("is_confidential")
		@Builder.Default
		private boolean confidential = false;
		
		@Field("is_archived")
		@Builder.Default
		private boolean archived = false;
		
		@CreatedDate
		@Field("created_at")
		private Instant createdAt;
		
		@LastModifiedDate
		@Field("updated_at")
		private Instant updatedAt;
		
		@Field("deleted_at")
		private Instant deletedAt;
		
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Prescription {
			private String medicationName;
			private String dosage;
			private String frequency;
			private String duration;
			private String instructions;
			private Instant prescribedAt;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class LabResult {
			private String testName;
			private String result;
			private String unit;
			private String referenceRange;
			private boolean abnormal;
			private Instant testedAt;
			private String laboratoryId;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class VitalSigns {
			private String bloodPressure;
			private double temperature;
			private int heartRate;
			private int respiratoryRate;
			private double weight;
			private double height;
			private double bmi;
			private int oxygenSaturation;
			private Instant recordedAt;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Attachment {
			private String fileName;
			private String fileUrl;
			private String fileType;
			private long fileSizeBytes;
			private Instant uploadedAt;
		}
}