package com.medicalSolutionsInc.entity.patientProfile;

import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
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
@Document(collection = "patient_profiles")
public class PatientProfile {

		@Id
		private String id;
		
		@Indexed(unique = true)
		@Field("patient_number")
		private String patientNumber;
		
		@Field("image_url")
		private String imageUrl;
		
		@Field("first_name")
		private String firstName;
		
		@Field("last_name")
		private String lastName;
		
		@Field("middle_name")
		private String middleName;
		
		@Field("date_of_birth")
		private Instant dateOfBirth;
		
		@Field("gender")
		@Builder.Default
		private GenderType gender = GenderType.FEMALE;
		
		@Field("blood_group")
		private BloodGroup bloodGroup;
		@Field("marital_status")
		private MaritalStatus maritalStatus;
		
		@Field("nationality")
		private String nationality;
		
		@Field("national_id")
		private String nationalId;
		
		@Field("occupation")
		private String occupation;
		
		@Indexed(unique = true)
		@Field("email")
		private String email;
		
		@Field("phone_number")
		private String phoneNumber;
		
		@Field("alternate_phone")
		private String alternatePhone;
		
		@Field("address")
		private Address address;
		
		@Field("emergency_contact")
		private EmergencyContact emergencyContact;
		
		@Field("allergies")
		@Builder.Default
		private List<String> allergies = new ArrayList<>();
		
		@Field("chronic_conditions")
		@Builder.Default
		private List<String> chronicConditions = new ArrayList<>();
		
		@Field("current_medications")
		@Builder.Default
		private List<String> currentMedications = new ArrayList<>();
		
		@Field("past_surgeries")
		@Builder.Default
		private List<String> pastSurgeries = new ArrayList<>();
		
		@Field("family_medical_history")
		@Builder.Default
		private List<String> familyMedicalHistory = new ArrayList<>();
		
		@Field("primary_doctor_id")
		private String primaryDoctorId;
		
		@Field("primary_doctor_name")
		private String primaryDoctorName;
		
		@Field("insurance")
		private Insurance insurance;
		
		@Field("is_active")
		@Builder.Default
		private boolean active = true;
		
		@Field("is_deceased")
		@Builder.Default
		private boolean deceased = false;
		
		@Field("deceased_at")
		private Instant deceasedAt;
		
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
		public static class Address {
			private String street;
			private String city;
			private String state;
			private String country;
			private String zipCode;
			private double latitude;
			private double longitude;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class EmergencyContact {
			private String fullName;
			private String relationship;
			private String phoneNumber;
			private String alternatePhone;
			private String email;
			private Address address;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Insurance {
			private String providerName;
			private String policyNumber;
			private String groupNumber;
			private String coverageType;
			private Instant expiryDate;
			private boolean active;
		}
}