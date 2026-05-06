package com.medicalSolutionsInc.dto.wardDTO;
public record VisitingHoursDTO(
		String morningStart,
		String morningEnd,
		String eveningStart,
		String eveningEnd,
		boolean visitorRestricted,
		int maxVisitorsAllowed
) {}