package com.medicalSolutionsInc.dto.medicalRecordDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Instant;

@Schema (name = "VitalSigns", description = "Patient vital signs")
public  record VitalSignsDTO(
		
		@Schema(description = "Blood pressure reading", example = "120/80 mmHg")
		String bloodPressure,
		
		@Schema(description = "Body temperature in Celsius", example = "36.6")
		double temperature,
		
		@Schema(description = "Heart rate in beats per minute", example = "72")
		@Min (value = 0, message = "Heart rate must be positive")
		int heartRate,
		
		@Schema(description = "Respiratory rate in breaths per minute", example = "16")
		@Min(value = 0, message = "Respiratory rate must be positive")
		int respiratoryRate,
		
		@Schema(description = "Weight in kilograms", example = "70.5")
		@DecimalMin (value = "0.0", message = "Weight must be positive")
		double weight,
		
		@Schema(description = "Height in centimetres", example = "175.0")
		@DecimalMin(value = "0.0", message = "Height must be positive")
		double height,
		
		@Schema(description = "Body Mass Index", example = "23.0")
		double bmi,
		
		@Schema(description = "Oxygen saturation percentage", example = "98")
		@Min(value = 0, message = "Oxygen saturation must be positive")
		@Max (value = 100, message = "Oxygen saturation cannot exceed 100")
		int oxygenSaturation,
		
		@Schema(description = "Time vital signs were recorded")
		Instant recordedAt
) {}