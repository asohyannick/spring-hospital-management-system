package com.medicalSolutionsInc.dto.userDTO;
public record GithubAuthResult(
		String accessToken,
		String refreshToken,
		UserResponseDTO user
) {}