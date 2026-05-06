package com.medicalSolutionsInc.repository.notification;

import com.medicalSolutionsInc.entity.notification.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
		
		List<Notification> findByRecipientIdOrderByCreatedAtDesc(String recipientId);
		
		List<Notification> findByRecipientIdAndReadIsFalseOrderByCreatedAtDesc(String recipientId);
		
		long countByRecipientIdAndReadIsFalse(String recipientId);
}