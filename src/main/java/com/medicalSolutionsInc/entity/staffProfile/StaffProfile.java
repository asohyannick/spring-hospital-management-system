package com.medicalSolutionsInc.entity.staffProfile;

import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.employmentType.EmploymentType;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import com.medicalSolutionsInc.enumerations.shiftType.ShiftType;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
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
@Document(collection = "staff_profiles")
public class StaffProfile {
		
		@Id
		private String id;
		
		@Indexed(unique = true)
		@Field("staff_number")
		private String staffNumber;
		
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
		
		@Field("nationality")
		private String nationality;
		
		@Field("national_id")
		private String nationalId;
		
		@Field("blood_group")
		private BloodGroup bloodGroup;
		
		@Field("marital_status")
		private MaritalStatus maritalStatus;
		
		@Indexed(unique = true)
		@Field("email")
		private String email;
		
		@Field("phone_number")
		private String phoneNumber;
		
		@Field("alternate_phone")
		private String alternatePhone;
		
		@Field("address")
		private Address address;
		
		@Field("role")
		private StaffRole role;
		
		@Field("department")
		private String department;
		
		@Field("specialization")
		private String specialization;
		
		@Field("employment_type")
		private EmploymentType employmentType;
		
		@Field("employment_status")
		@Builder.Default
		private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE;
		
		@Field("hire_date")
		private Instant hireDate;
		
		@Field("termination_date")
		private Instant terminationDate;
		
		@Field("facility_id")
		private String facilityId;
		
		@Field("facility_name")
		private String facilityName;
		
		@Field("supervisor_id")
		private String supervisorId;
		
		@Field("supervisor_name")
		private String supervisorName;
		
		@Field("description")
		private String description;
		
		@Field("years_of_experience")
		private int yearsOfExperience;
		
		@Field("license_number")
		@Indexed(unique = true)
		private String licenseNumber;
		
		@Field("license_expiry_date")
		private Instant licenseExpiryDate;
		
		@Field("certifications")
		@Builder.Default
		private List<Certification> certifications = new ArrayList<>();
		
		@Field("education")
		@Builder.Default
		private List<Education> education = new ArrayList<>();
		
		@Field("languages_spoken")
		@Builder.Default
		private List<String> languagesSpoken = new ArrayList<>();
		
		@Field("skills")
		@Builder.Default
		private List<String> skills = new ArrayList<>();
		
		@Field("hobbies")
		@Builder.Default
		private List<String> hobbies = new ArrayList<>();
		
		@Field("emergency_contact")
		private EmergencyContact emergencyContact;
		
		@Field("shift")
		private ShiftType shift;
		
		@Field("working_days")
		@Builder.Default
		private List<String> workingDays = new ArrayList<>();
		
		@Field("is_verified")
		@Builder.Default
		private boolean verified = false;
		
		@Field("is_available")
		@Builder.Default
		private boolean available = true;
		
		@Field("accepts_new_patients")
		@Builder.Default
		private boolean acceptsNewPatients = true;
		
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
		public static class Certification {
			private String name;
			private String issuingBody;
			private String certificateNumber;
			private Instant issuedAt;
			private Instant expiresAt;
			private boolean active;
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Education {
			private String degree;
			private String institution;
			private String fieldOfStudy;
			private int graduationYear;
			private String country;
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
		}

}