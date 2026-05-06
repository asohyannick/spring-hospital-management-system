package com.medicalSolutionsInc.entity.pharmacy;

import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
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
@Document(collection = "pharmacies")
public class Pharmacy {

		@Id
		private String id;
		
		@Indexed(unique = true)
		@Field("pharmacy_number")
		private String pharmacyNumber;
		
		@Field("name")
		private String name;
		
		@Indexed(unique = true)
		@Field("license_number")
		private String licenseNumber;
		
		@Field("type")
		private PharmacyType type;
		
		@Field("status")
		@Builder.Default
		private PharmacyStatus status = PharmacyStatus.ACTIVE;
		
		@Field("description")
		private String description;
		
		@Field("image_url")
		private String imageUrl;
		
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
		
		@Field("operating_hours")
		private String operatingHours;
		
		@Field("operating_days")
		@Builder.Default
		private List<String> operatingDays = new ArrayList<>();
		
		@Field("services_offered")
		@Builder.Default
		private List<String> servicesOffered = new ArrayList<>();
		
		@Field("accepted_insurance_providers")
		@Builder.Default
		private List<String> acceptedInsuranceProviders = new ArrayList<>();
		
		@Field("accepted_payment_methods")
		@Builder.Default
		private List<String> acceptedPaymentMethods = new ArrayList<>();
		
		@Field("head_pharmacist")
		private String headPharmacist;
		
		@Field("contact_person")
		private String contactPerson;
		
		@Field("staff_count")
		private int staffCount;
		
		@Field("total_medications_in_stock")
		@Builder.Default
		private int totalMedicationsInStock = 0;
		
		@Field("low_stock_threshold")
		@Builder.Default
		private int lowStockThreshold = 10;
		
		@Field("is_verified")
		@Builder.Default
		private boolean verified = false;
		
		@Field("is_24_hours")
		@Builder.Default
		private boolean open24Hours = false;
		
		@Field("accepts_walk_ins")
		@Builder.Default
		private boolean acceptsWalkIns = true;
		
		@Field("accepts_online_orders")
		@Builder.Default
		private boolean acceptsOnlineOrders = false;
		
		@Field("offers_delivery")
		@Builder.Default
		private boolean offersDelivery = false;
		
		@Field("delivery_radius_km")
		private double deliveryRadiusKm;
		
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