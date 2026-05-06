package com.medicalSolutionsInc.dto.notificationDTO;
import com.medicalSolutionsInc.enumerations.notificationPriority.NotificationPriority;
import com.medicalSolutionsInc.enumerations.notificationStatus.NotificationStatus;
import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import com.medicalSolutionsInc.enumerations.recipientRole.RecipientRole;
import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;

import java.time.Instant;

public record CreateNotificationResponseDTO(
		
		String id,
		String title,
		String message,
		NotificationType type,
		NotificationStatus status,
		NotificationPriority priority,
		String recipientId,
		String recipientName,
		String recipientEmail,
		String recipientPhone,
		RecipientRole recipientRole,
		String senderId,
		String senderName,
		String referenceId,
		ReferenceType referenceType,
		Instant scheduledAt,
		Instant sentAt,
		Instant deliveredAt,
		Instant readAt,
		int retryCount,
		int maxRetries,
		String failureReason,
		boolean read,
		boolean archived,
		boolean requiresAction,
		String actionUrl,
		Instant createdAt,
		Instant updatedAt
) {}