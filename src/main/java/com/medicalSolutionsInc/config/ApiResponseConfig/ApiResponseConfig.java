package com.medicalSolutionsInc.config.ApiResponseConfig;

import java.time.Instant;

public record ApiResponseConfig<T>(
		Instant timestamp,
		String message,
		T data,
		int statusCode
) {}