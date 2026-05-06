package com.medicalSolutionsInc.dto.notificationDTO;

import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Request payload for subscribing to notifications")
public record SubscriptionRequestDTO(
		
		@NotBlank(message = "Recipient ID is required")
		@Schema(description = "ID of the user subscribing", example = "usr_abc123")
		String recipientId,
		
		@Schema(description = "Display name of the recipient", example = "John Doe")
		String recipientName,
		
		@Email(message = "Invalid email format")
		@Schema(description = "Email address for email notifications", example = "john.doe@example.com")
		String recipientEmail,
		
		@Schema(description = "Phone number in E.164 format for SMS notifications", example = "+237690000000")
		String recipientPhone,
		
		@NotEmpty(message = "At least one notification channel must be selected")
		@Schema(description = "Channels to subscribe to", example = "[\"EMAIL\", \"SMS\", \"IN_APP\"]")
		List<NotificationType> subscribedChannels,
		
		@Schema(description = "Specific topics to subscribe to, e.g. APPOINTMENT_REMINDER, LAB_RESULT",
				example = "[\"APPOINTMENT_REMINDER\", \"LAB_RESULT\"]")
		List<String> subscribedTopics
) {}