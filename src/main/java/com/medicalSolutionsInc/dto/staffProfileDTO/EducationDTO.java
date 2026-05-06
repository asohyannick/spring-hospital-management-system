package com.medicalSolutionsInc.dto.staffProfileDTO;
import jakarta.validation.constraints.NotBlank;

public record EducationDTO(
		@NotBlank String degree,
		@NotBlank String institution,
		String fieldOfStudy,
		int graduationYear,
		String country
) {}