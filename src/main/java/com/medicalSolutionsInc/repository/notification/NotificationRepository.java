package com.medicalSolutionsInc.repository.notification;
import com.medicalSolutionsInc.entity.notification.Notification;
import com.medicalSolutionsInc.enumerations.notificationPriority.NotificationPriority;
import com.medicalSolutionsInc.enumerations.notificationStatus.NotificationStatus;
import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import com.medicalSolutionsInc.enumerations.recipientRole.RecipientRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

		List<Notification> findByRecipientId(String recipientId);
		
		List<Notification> findByRecipientEmail(String recipientEmail);
		
		List<Notification> findByRecipientRole(RecipientRole recipientRole);
		
		List<Notification> findByRecipientIdAndReadFalse(String recipientId);
		
		List<Notification> findByRecipientIdAndArchivedFalse(String recipientId);
		
		List<Notification> findByRecipientIdAndStatus(String recipientId, NotificationStatus status);
		
		long countByRecipientIdAndReadFalse(String recipientId);
		
		List<Notification> findByType(NotificationType type);
		
		List<Notification> findByPriority(NotificationPriority priority);
		
		List<Notification> findByRecipientIdAndPriority(String recipientId, NotificationPriority priority);
		
		List<Notification> findByRecipientIdAndRequiresActionTrue(String recipientId);
		
		List<Notification> findBySenderId(String senderId);
		
		List<Notification> findByReferenceId(String referenceId);
		
		List<Notification> findByStatusAndScheduledAtBefore(NotificationStatus status, Instant time);
		
		List<Notification> findByStatusAndRetryCountLessThan(NotificationStatus status, int maxRetries);
}