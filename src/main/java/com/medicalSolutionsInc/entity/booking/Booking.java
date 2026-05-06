package com.medicalSolutionsInc.entity.booking;

import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bookings")
public class Booking {

		@Id
		private String id;
		private String imageURL;
		private String firstName;
		private String lastName;
		private Integer age;
		private String email;
		private String phone;
		private String address;
		private String city;
		private String state;
		private String zipCode;
		private String country;
		private String profession;
		private Instant checkInTime;
		private Instant checkOutTime;
		private MaritalStatus maritalStatus;
		private LocalDate birthDate;
		private EmploymentStatus employmentStatus;
		private String gender;
		private String emergencyContactName;
		private String emergencyContactPhone;
		private String insuranceProvider;
		private String insuranceNumber;
		private String notes;
		private List<String> hobbies = new ArrayList<>();
		private List<String> allergies = new ArrayList<>();
		private List<String> diseases = new ArrayList<>();
		private List<String> symptoms = new ArrayList<>();
		private List<String> previousMedications = new ArrayList<>();
		private List<String> academicLevel = new ArrayList<>();
		private BookingStatus bookingStatus;
		private Instant createdAt;
		private Instant updatedAt;
}