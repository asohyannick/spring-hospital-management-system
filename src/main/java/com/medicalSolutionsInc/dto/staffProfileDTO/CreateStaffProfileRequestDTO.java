package com.medicalSolutionsInc.dto.staffProfileDTO;

import com.medicalSolutionsInc.enumerations.bloodGroup.BloodGroup;
import com.medicalSolutionsInc.enumerations.employmentStatus.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.employmentType.EmploymentType;
import com.medicalSolutionsInc.enumerations.genderType.GenderType;
import com.medicalSolutionsInc.enumerations.maritalStatus.MaritalStatus;
import com.medicalSolutionsInc.enumerations.shiftType.ShiftType;
import com.medicalSolutionsInc.enumerations.staffRole.StaffRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;

public record CreateStaffProfileRequestDTO(
		
		@NotBlank(message = "First name is required")
		String firstName,
		
		@NotBlank(message = "Last name is required")
		String lastName,
		
		String middleName,
		
		@NotNull(message = "Date of birth is required")
		@Past(message = "Date of birth must be in the past")
		Instant dateOfBirth,
		
		@NotNull(message = "Gender is required")
		GenderType gender,
		
		String nationality,
		String nationalId,
		BloodGroup bloodGroup,
		MaritalStatus maritalStatus,
		
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		String email,
		
		@NotBlank(message = "Phone number is required")
		String phoneNumber,
		
		String alternatePhone,
		
		@Valid
		AddressDTO address,
		
		@NotNull(message = "Role is required")
		StaffRole role,
		
		String department,
		String specialization,
		
		@NotNull(message = "Employment type is required")
		EmploymentType employmentType,
		
		EmploymentStatus employmentStatus,
		
		@NotNull(message = "Hire date is required")
		Instant hireDate,
		
		String facilityId,
		String facilityName,
		String supervisorId,
		String supervisorName,
		String description,
		
		@Min(value = 0, message = "Years of experience cannot be negative")
		int yearsOfExperience,
		
		String licenseNumber,
		Instant licenseExpiryDate,
		
		@Valid
		List<CertificationDTO> certifications,
		
		@Valid
		List<EducationDTO> education,
		
		List<String> languagesSpoken,
		List<String> skills,
		List<String> hobbies,
		
		@Valid
		EmergencyContactDTO emergencyContact,
		
		ShiftType shift,
		List<String> workingDays,
		
		String imageUrl
) {}