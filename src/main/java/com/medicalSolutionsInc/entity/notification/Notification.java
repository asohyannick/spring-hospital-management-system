package com.medicalSolutionsInc.entity.notification;

import com.medicalSolutionsInc.enumerations.notificationPriority.NotificationPriority;
import com.medicalSolutionsInc.enumerations.notificationStatus.NotificationStatus;
import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import com.medicalSolutionsInc.enumerations.recipientRole.RecipientRole;
import com.medicalSolutionsInc.enumerations.referenceType.ReferenceType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications")
public class Notification {

		@Id
		private String id;
		
		@Field("title")
		private String title;
		
		@Field("message")
		private String message;
		
		@Field("type")
		@Builder.Default
		private NotificationType type = NotificationType.SMS;
		
		@Field("status")
		@Builder.Default
		private NotificationStatus status = NotificationStatus.PENDING;
		
		@Field("priority")
		@Builder.Default
		private NotificationPriority priority = NotificationPriority.NORMAL;
		
		@Indexed
		@Field("recipient_id")
		private String recipientId;
		
		@Field("recipient_name")
		private String recipientName;
		
		@Indexed
		@Field("recipient_email")
		private String recipientEmail;
		
		@Field("recipient_phone")
		private String recipientPhone;
		
		@Field("recipient_role")
		private RecipientRole recipientRole;
		
		@Field("sender_id")
		private String senderId;
		
		@Field("sender_name")
		private String senderName;
		
		@Field("reference_id")
		private String referenceId;
		
		@Field("reference_type")
		private ReferenceType referenceType;
		
		@Field("scheduled_at")
		private Instant scheduledAt;
		
		@Field("sent_at")
		private Instant sentAt;
		
		@Field("delivered_at")
		private Instant deliveredAt;
		
		@Field("read_at")
		private Instant readAt;
		
		@Field("retry_count")
		@Builder.Default
		private int retryCount = 0;
		
		@Field("max_retries")
		@Builder.Default
		private int maxRetries = 3;
		
		@Field("failure_reason")
		private String failureReason;
		
		@Field("is_read")
		@Builder.Default
		private boolean read = false;
		
		@Field("is_archived")
		@Builder.Default
		private boolean archived = false;
		
		@Field("requires_action")
		@Builder.Default
		private boolean requiresAction = false;
		
		@Field("action_url")
		private String actionUrl;
		
		@CreatedDate
		@Field("created_at")
		private Instant createdAt;
		
		@LastModifiedDate
		@Field("updated_at")
		private Instant updatedAt;
		
}