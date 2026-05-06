package com.medicalSolutionsInc.dto.notificationDTO;
import com.medicalSolutionsInc.enumerations.notificationPriority.NotificationPriority;
import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import com.medicalSolutionsInc.enumerations.recipientRole.RecipientRole;
import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateNotificationRequestDTO(
		
		@NotBlank(message = "Title is required")
		String title,
		
		@NotBlank(message = "Message is required")
		String message,
		
		@NotNull(message = "Notification type is required")
		NotificationType type,
		
		NotificationPriority priority,
		
		@NotBlank(message = "Recipient ID is required")
		String recipientId,
		
		String recipientName,
		
		@Email(message = "Invalid recipient email format")
		String recipientEmail,
		
		String recipientPhone,
		
		RecipientRole recipientRole,
		
		String senderId,
		String senderName,
		
		String referenceId,
		ReferenceType referenceType,
		
		Instant scheduledAt,
		
		boolean requiresAction,
		String actionUrl
) {}