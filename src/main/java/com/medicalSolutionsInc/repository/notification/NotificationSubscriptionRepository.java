package com.medicalSolutionsInc.repository.notification;

import com.medicalSolutionsInc.entity.notification.NotificationSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationSubscriptionRepository extends MongoRepository<NotificationSubscription, String> {
		
		Optional<NotificationSubscription> findByRecipientIdAndDeletedAtIsNull(String recipientId);
		
		List<NotificationSubscription> findByActiveIsTrueAndDeletedAtIsNull();
		
		boolean existsByRecipientIdAndDeletedAtIsNull(String recipientId);
}