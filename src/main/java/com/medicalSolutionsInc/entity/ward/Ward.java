package com.medicalSolutionsInc.entity.ward;

import com.medicalSolutionsInc.enumerations.genderRestriction.GenderRestriction;
import com.medicalSolutionsInc.enumerations.wardStatus.WardStatus;
import com.medicalSolutionsInc.enumerations.wardType.WardType;
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
@Document(collection = "wards")
public class Ward {

		@Id
		private String id;
		
		@Indexed(unique = true)
		@Field("ward_number")
		private String wardNumber;
		
		@Field("name")
		private String name;
		
		@Field("type")
		private WardType type;
		
		@Field("status")
		@Builder.Default
		private WardStatus status = WardStatus.OPERATIONAL;
		
		@Field("description")
		private String description;
		
		@Field("floor")
		private String floor;
		
		@Field("building")
		private String building;
		
		@Field("facility_id")
		private String facilityId;
		
		@Field("facility_name")
		private String facilityName;
		
		@Field("total_beds")
		private int totalBeds;
		
		@Field("available_beds")
		@Builder.Default
		private int availableBeds = 0;
		
		@Field("occupied_beds")
		@Builder.Default
		private int occupiedBeds = 0;
		
		@Field("reserved_beds")
		@Builder.Default
		private int reservedBeds = 0;
		
		@Field("beds")
		@Builder.Default
		private List<Bed> beds = new ArrayList<>();
		
		@Field("ward_in_charge_id")
		private String wardInChargeId;
		
		@Field("ward_in_charge_name")
		private String wardInChargeName;
		
		@Field("assigned_doctor_ids")
		@Builder.Default
		private List<String> assignedDoctorIds = new ArrayList<>();
		
		@Field("assigned_nurse_ids")
		@Builder.Default
		private List<String> assignedNurseIds = new ArrayList<>();
		
		@Field("staff_count")
		private int staffCount;
		
		@Field("equipment")
		@Builder.Default
		private List<String> equipment = new ArrayList<>();
		
		@Field("amenities")
		@Builder.Default
		private List<String> amenities = new ArrayList<>();
		
		@Field("gender_restriction")
		private GenderRestriction genderRestriction;
		
		@Field("visiting_hours")
		private VisitingHours visitingHours;
		
		@Field("is_isolation_ward")
		@Builder.Default
		private boolean isolationWard = false;
		
		@Field("is_private")
		@Builder.Default
		private boolean privateWard = false;
		
		@Field("is_pediatric")
		@Builder.Default
		private boolean pediatric = false;
		
		@Field("accepts_emergency")
		@Builder.Default
		private boolean acceptsEmergency = false;
		
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
		public static class Bed {
			private String bedNumber;
			private BedStatus status;
			private String patientId;
			private String patientName;
			private Instant admittedAt;
			private Instant expectedDischargeAt;
			private boolean hasMonitor;
			private boolean hasVentilator;
			private boolean hasOxygen;
			
			public enum BedStatus {
				AVAILABLE,
				OCCUPIED,
				RESERVED,
				UNDER_MAINTENANCE
			}
		}
		
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class VisitingHours {
			private String morningStart;
			private String morningEnd;
			private String eveningStart;
			private String eveningEnd;
			private boolean visitorRestricted;
			private int maxVisitorsAllowed;
		}

}