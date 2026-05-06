package com.medicalSolutionsInc.entity.laboratory;

import com.medicalSolutionsInc.enumerations.laboratoryStatus.LaboratoryStatus;
import com.medicalSolutionsInc.enumerations.laboratoryType.LaboratoryType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
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
@Document(collection = "laboratories")
public class Laboratory {

		@Id
		private String id;
		
		@Field("name")
		private String name;
		
		@Field("registration_number")
		@Indexed(unique = true)
		private String registrationNumber;
		
		@Field("accreditation_number")
		private String accreditationNumber;
		
		@Field("type")
		private LaboratoryType type;
		
		@Field("status")
		@Builder.Default
		private LaboratoryStatus status = LaboratoryStatus.ACTIVE;
		
		@Indexed(unique = true)
		@Field("email")
		private String email;
		
		@Field("phone")
		private String phone;
		
		@Field("alternate_phone")
		private String alternatePhone;
		
		@Field("fax")
		private String fax;
		
		@Field("website")
		private String website;
		
		@Field("address")
		private Address address;
		
		@Field("description")
		private String description;
		
		@Field("image_url")
		private String imageUrl;
		
		@Field("opening_hours")
		private String openingHours;
		
		@Field("tests_offered")
		@Builder.Default
		private List<String> testsOffered = new ArrayList <> ();
		
		@Field("operating_days")
		@Builder.Default
		private List<String> operatingDays = new ArrayList<>();
		
		@Field("head_pathologist")
		private String headPathologist;
		
		@Field("contact_person")
		private String contactPerson;
		
		@Field("accepts_walk_ins")
		@Builder.Default
		private boolean acceptsWalkIns = true;
		
		@Field("accepts_online_booking")
		@Builder.Default
		private boolean acceptsOnlineBooking = false;
		
		@Field("is_verified")
		@Builder.Default
		private boolean verified = false;
		
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
}