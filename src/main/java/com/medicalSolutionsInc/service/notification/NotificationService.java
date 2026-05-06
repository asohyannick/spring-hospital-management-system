package com.medicalSolutionsInc.service.notification;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.notificationDTO.*;
import com.medicalSolutionsInc.entity.notification.Notification;
import com.medicalSolutionsInc.entity.notification.NotificationSubscription;
import com.medicalSolutionsInc.enumerations.notificationStatus.NotificationStatus;
import com.medicalSolutionsInc.enumerations.notificationType.NotificationType;
import com.medicalSolutionsInc.mappers.notificationMapper.NotificationMapper;
import com.medicalSolutionsInc.mappers.notificationMapper.NotificationSubscriptionMapper;
import com.medicalSolutionsInc.repository.notification.NotificationRepository;
import com.medicalSolutionsInc.repository.notification.NotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

		private final NotificationRepository notificationRepository;
		private final NotificationSubscriptionRepository subscriptionRepository;
		private final NotificationMapper notificationMapper;
		private final NotificationSubscriptionMapper subscriptionMapper;
		private final EmailService emailService;
		private final SmsService smsService;
		
		// ─── Send Notification (EMAIL) ────────────────────────────────────────────────
		
		/**
		 * Sends an email notification to the specified recipient and persists a record.
		 * Validates that the recipient has an active subscription with EMAIL channel before dispatching.
		 */
		public ApiResponseConfig<CreateNotificationResponseDTO> sendEmailNotification(CreateNotificationRequestDTO request) {
			log.info("Sending EMAIL notification to recipient: {}", request.recipientId());
			
			validateChannelSubscription(request.recipientId(), NotificationType.EMAIL);
			
			Notification notification = buildNotification(request, NotificationType.EMAIL);
			
			try {
				String htmlBody = emailService.buildHtmlTemplate(
						request.recipientName(),
						request.title(),
						request.message(),
						request.actionUrl()
				);
				emailService.sendEmail(request.recipientEmail(), request.title(), htmlBody);
				
				notification.setStatus(NotificationStatus.SENT);
				notification.setSentAt(Instant.now());
				log.info("EMAIL notification dispatched to: {}", request.recipientEmail());
				
			} catch (Exception e) {
				log.error("EMAIL notification failed for recipient {}: {}", request.recipientId(), e.getMessage());
				notification.setStatus(NotificationStatus.FAILED);
				notification.setFailureReason(e.getMessage());
				notification.setRetryCount(notification.getRetryCount() + 1);
			}
			
			Notification saved = notificationRepository.save(notification);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Email notification processed",
					notificationMapper.toResponseDTO(saved),
					HttpStatus.OK.value()
			);
		}
		
		// ─── Send Notification (SMS) ──────────────────────────────────────────────────
		
		/**
		 * Sends an SMS notification to the specified recipient and persists a record.
		 * Validates that the recipient has an active subscription with SMS channel before dispatching.
		 */
		public ApiResponseConfig<CreateNotificationResponseDTO> sendSmsNotification(CreateNotificationRequestDTO request) {
			log.info("Sending SMS notification to recipient: {}", request.recipientId());
			
			validateChannelSubscription(request.recipientId(), NotificationType.SMS);
			
			Notification notification = buildNotification(request, NotificationType.SMS);
			
			try {
				String smsBody = smsService.formatSmsBody(request.title(), request.message());
				smsService.sendSms(request.recipientPhone(), smsBody);
				
				notification.setStatus(NotificationStatus.SENT);
				notification.setSentAt(Instant.now());
				log.info("SMS notification dispatched to: {}", request.recipientPhone());
				
			} catch (Exception e) {
				log.error("SMS notification failed for recipient {}: {}", request.recipientId(), e.getMessage());
				notification.setStatus(NotificationStatus.FAILED);
				notification.setFailureReason(e.getMessage());
				notification.setRetryCount(notification.getRetryCount() + 1);
			}
			
			Notification saved = notificationRepository.save(notification);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"SMS notification processed",
					notificationMapper.toResponseDTO(saved),
					HttpStatus.OK.value()
			);
		}
		
		// ─── Send Notification (IN-APP) ───────────────────────────────────────────────
		
		/**
		 * Saves an in-app notification for a recipient. No external dispatch; the client
		 * polls or uses WebSocket to surface unread in-app notifications.
		 */
		public ApiResponseConfig<CreateNotificationResponseDTO> sendInAppNotification(CreateNotificationRequestDTO request) {
			log.info("Creating IN-APP notification for recipient: {}", request.recipientId());
			
			validateChannelSubscription(request.recipientId(), NotificationType.IN_APP);
			
			Notification notification = buildNotification(request, NotificationType.IN_APP);
			notification.setStatus(NotificationStatus.DELIVERED);
			notification.setSentAt(Instant.now());
			notification.setDeliveredAt(Instant.now());
			
			Notification saved = notificationRepository.save(notification);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"In-app notification created successfully",
					notificationMapper.toResponseDTO(saved),
					HttpStatus.CREATED.value()
			);
		}
		
		// ─── Broadcast to All Channels ────────────────────────────────────────────────
		
		/**
		 * Sends a notification across ALL channels (EMAIL + SMS + IN_APP) the recipient
		 * is subscribed to. Each channel is attempted independently — a failure on one
		 * does not abort the others.
		 */
		public ApiResponseConfig<List<CreateNotificationResponseDTO>> broadcastNotification(CreateNotificationRequestDTO request) {
			log.info("Broadcasting notification to all subscribed channels for recipient: {}", request.recipientId());
			
			NotificationSubscription subscription = subscriptionRepository
					                                        .findByRecipientIdAndDeletedAtIsNull(request.recipientId())
					                                        .orElseThrow(() -> new ResponseStatusException(
							                                        HttpStatus.NOT_FOUND,
							                                        "No active subscription found for recipient: " + request.recipientId()
					                                        ));
			
			List<CreateNotificationResponseDTO> results = subscription.getSubscribedChannels()
					                                              .stream()
					                                              .map(channel -> dispatchToChannel(request, channel))
					                                              .toList();
			
			log.info("Broadcast complete — {} channel(s) attempted for recipient: {}", results.size(), request.recipientId());
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Notification broadcast completed",
					results,
					HttpStatus.OK.value()
			);
		}
		
		// ─── Subscribe ────────────────────────────────────────────────────────────────
		
		/**
		 * Subscribes a customer to notification channels and topics.
		 * If the recipient already has a subscription, it is updated (upsert behaviour).
		 */
		public ApiResponseConfig<SubscriptionResponseDTO> subscribe(SubscriptionRequestDTO request) {
			log.info("Processing subscription for recipient: {}", request.recipientId());
			
			boolean exists = subscriptionRepository.existsByRecipientIdAndDeletedAtIsNull(request.recipientId());
			
			if (exists) {
				NotificationSubscription existing = subscriptionRepository
						                                    .findByRecipientIdAndDeletedAtIsNull(request.recipientId())
						                                    .orElseThrow();
				
				subscriptionMapper.updateEntity(request, existing);
				NotificationSubscription updated = subscriptionRepository.save(existing);
				
				log.info("Subscription updated for recipient: {}", request.recipientId());
				return new ApiResponseConfig<>(
						Instant.now(),
						"Subscription updated successfully",
						subscriptionMapper.toResponseDTO(updated),
						HttpStatus.OK.value()
				);
			}
			
			NotificationSubscription subscription = subscriptionMapper.toEntity(request);
			subscription.setActive(true);
			subscription.setCreatedAt(Instant.now());
			subscription.setUpdatedAt(Instant.now());
			
			NotificationSubscription saved = subscriptionRepository.save(subscription);
			log.info("New subscription created for recipient: {}", request.recipientId());
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Subscribed to notifications successfully",
					subscriptionMapper.toResponseDTO(saved),
					HttpStatus.CREATED.value()
			);
		}
		
		// ─── Unsubscribe ──────────────────────────────────────────────────────────────
		
		/**
		 * Soft-deletes the recipient's subscription, effectively opting them out
		 * of all notification channels.
		 */
		public ApiResponseConfig<Void> unsubscribe(String recipientId) {
			log.info("Unsubscribing recipient: {}", recipientId);
			
			NotificationSubscription subscription = subscriptionRepository
					                                        .findByRecipientIdAndDeletedAtIsNull(recipientId)
					                                        .orElseThrow(() -> new ResponseStatusException(
							                                        HttpStatus.NOT_FOUND,
							                                        "No active subscription found for recipient: " + recipientId
					                                        ));
			
			subscription.setActive(false);
			subscription.setDeletedAt(Instant.now());
			subscriptionRepository.save(subscription);
			
			log.info("Recipient {} successfully unsubscribed", recipientId);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Unsubscribed successfully",
					null,
					HttpStatus.OK.value()
			);
		}
		
		// ─── Fetch Subscription ───────────────────────────────────────────────────────
		
		public ApiResponseConfig<SubscriptionResponseDTO> fetchSubscription(String recipientId) {
			log.info("Fetching subscription for recipient: {}", recipientId);
			
			NotificationSubscription subscription = subscriptionRepository
					                                        .findByRecipientIdAndDeletedAtIsNull(recipientId)
					                                        .orElseThrow(() -> new ResponseStatusException(
							                                        HttpStatus.NOT_FOUND,
							                                        "No subscription found for recipient: " + recipientId
					                                        ));
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Subscription fetched successfully",
					subscriptionMapper.toResponseDTO(subscription),
					HttpStatus.OK.value()
			);
		}
		
		// ─── Mark Notification as Read ────────────────────────────────────────────────
		
		public ApiResponseConfig<CreateNotificationResponseDTO> markAsRead(String notificationId) {
			log.info("Marking notification as read: {}", notificationId);
			
			Notification notification = findNotificationOrThrow(notificationId);
			notification.setRead(true);
			notification.setReadAt(Instant.now());
			notification.setUpdatedAt(Instant.now());
			
			Notification updated = notificationRepository.save(notification);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Notification marked as read",
					notificationMapper.toResponseDTO(updated),
					HttpStatus.OK.value()
			);
		}
		
		// ─── Archive Notification ─────────────────────────────────────────────────────
		
		public ApiResponseConfig<CreateNotificationResponseDTO> archiveNotification(String notificationId) {
			log.info("Archiving notification: {}", notificationId);
			
			Notification notification = findNotificationOrThrow(notificationId);
			notification.setArchived(true);
			notification.setUpdatedAt(Instant.now());
			
			Notification updated = notificationRepository.save(notification);
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Notification archived successfully",
					notificationMapper.toResponseDTO(updated),
					HttpStatus.OK.value()
			);
		}
		
		// ─── Fetch Notifications for Recipient ───────────────────────────────────────
		
		public ApiResponseConfig<List<CreateNotificationResponseDTO>> fetchNotificationsForRecipient(String recipientId) {
			log.info("Fetching notifications for recipient: {}", recipientId);
			
			List<CreateNotificationResponseDTO> notifications = notificationRepository
					                                                    .findByRecipientIdOrderByCreatedAtDesc(recipientId)
					                                                    .stream()
					                                                    .map(notificationMapper::toResponseDTO)
					                                                    .toList();
			
			return new ApiResponseConfig<>(
					Instant.now(),
					"Notifications fetched successfully",
					notifications,
					HttpStatus.OK.value()
			);
		}
		
		// ─── Private Helpers ─────────────────────────────────────────────────────────
		
		private Notification buildNotification(CreateNotificationRequestDTO request, NotificationType channel) {
			Notification notification = notificationMapper.toEntity(request);
			notification.setType(channel);
			notification.setStatus(NotificationStatus.PENDING);
			notification.setCreatedAt(Instant.now());
			notification.setUpdatedAt(Instant.now());
			return notification;
		}
		
		private CreateNotificationResponseDTO dispatchToChannel(CreateNotificationRequestDTO request, NotificationType channel) {
			try {
				return switch (channel) {
					case EMAIL  -> sendEmailNotification(request).data();
					case SMS    -> sendSmsNotification(request).data();
					case IN_APP -> sendInAppNotification(request).data();
					default -> {
						log.warn("Unsupported broadcast channel: {}", channel);
						yield null;
					}
				};
			} catch (Exception e) {
				log.error("Broadcast failed on channel {} for recipient {}: {}", channel, request.recipientId(), e.getMessage());
				Notification failed = buildNotification(request, channel);
				failed.setStatus(NotificationStatus.FAILED);
				failed.setFailureReason(e.getMessage());
				return notificationMapper.toResponseDTO(notificationRepository.save(failed));
			}
		}
		
		private void validateChannelSubscription(String recipientId, NotificationType channel) {
			NotificationSubscription subscription = subscriptionRepository
					                                        .findByRecipientIdAndDeletedAtIsNull(recipientId)
					                                        .orElseThrow(() -> new ResponseStatusException(
							                                        HttpStatus.FORBIDDEN,
							                                        "Recipient " + recipientId + " has no active notification subscription"
					                                        ));
			
			boolean subscribed = subscription.getSubscribedChannels() != null
					                     && subscription.getSubscribedChannels().contains(channel);
			
			if (!subscribed) {
				throw new ResponseStatusException(
						HttpStatus.FORBIDDEN,
						"Recipient " + recipientId + " is not subscribed to channel: " + channel
				);
			}
		}
		
		private Notification findNotificationOrThrow(String notificationId) {
			return notificationRepository.findById(notificationId)
					       .orElseThrow(() -> new ResponseStatusException(
							       HttpStatus.NOT_FOUND,
							       "Notification not found with ID: " + notificationId
					       ));
		}
}