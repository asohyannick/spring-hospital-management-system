package com.medicalSolutionsInc.entity.notification;

import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notification_subscriptions")
@CompoundIndex(name = "recipient_unique_idx", def = "{'recipient_id': 1, 'deleted_at': 1}")
public class NotificationSubscription {

		@Id
		private String id;
		
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
		
		@Field("subscribed_channels")
		@Builder.Default
		private List<NotificationType> subscribedChannels = List.of(NotificationType.IN_APP);
		
		@Field("subscribed_topics")
		private List<String> subscribedTopics;
		
		@Field("email_verified")
		@Builder.Default
		private boolean emailVerified = false;
		
		@Field("phone_verified")
		@Builder.Default
		private boolean phoneVerified = false;
		
		@Field("active")
		@Builder.Default
		private boolean active = true;
		
		@CreatedDate
		@Field("created_at")
		private Instant createdAt;
		
		@LastModifiedDate
		@Field("updated_at")
		private Instant updatedAt;
		
		@Field("deleted_at")
		private Instant deletedAt;
}