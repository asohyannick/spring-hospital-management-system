package com.medicalSolutionsInc.dto.notificationDTO;

import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Response payload for a notification subscription")
public record SubscriptionResponseDTO(
		
		String id,
		String recipientId,
		String recipientName,
		String recipientEmail,
		String recipientPhone,
		List<NotificationType> subscribedChannels,
		List<String> subscribedTopics,
		boolean emailVerified,
		boolean phoneVerified,
		boolean active,
		Instant createdAt,
		Instant updatedAt
) {}