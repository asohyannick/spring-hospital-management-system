package com.medicalSolutionsInc.controller.notificationController;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.notificationDTO.CreateNotificationRequestDTO;
import com.medicalSolutionsInc.dto.notificationDTO.CreateNotificationResponseDTO;
import com.medicalSolutionsInc.dto.notificationDTO.SubscriptionRequestDTO;
import com.medicalSolutionsInc.dto.notificationDTO.SubscriptionResponseDTO;
import com.medicalSolutionsInc.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${api.version}/notify")
@Tag(name = "Notification Management Endpoints", description = "Send SMS, email, and in-app notifications, and manage customer subscriptions")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class NotificationController {

		private final NotificationService notificationService;
		
		@Operation(
				summary = "Send an email notification",
				description = "Dispatches an HTML email to the specified recipient. Recipient must have an active subscription with the EMAIL channel enabled. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Email notification processed",
						content = @Content(schema = @Schema(implementation = ApiResponseConfig.class))),
				@ApiResponse(responseCode = "400", description = "Invalid request payload"),
				@ApiResponse(responseCode = "403", description = "Recipient not subscribed to EMAIL channel"),
				@ApiResponse(responseCode = "404", description = "Recipient subscription not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@PostMapping("/email")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<CreateNotificationResponseDTO> sendEmailNotification(
				@Valid @RequestBody CreateNotificationRequestDTO request
		) {
			return notificationService.sendEmailNotification(request);
		}
		
		@Operation(
				summary = "Send an SMS notification",
				description = "Dispatches an SMS to the recipient's phone number via Twilio. Recipient must have an active subscription with the SMS channel enabled. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "SMS notification processed"),
				@ApiResponse(responseCode = "400", description = "Invalid request payload"),
				@ApiResponse(responseCode = "403", description = "Recipient not subscribed to SMS channel"),
				@ApiResponse(responseCode = "404", description = "Recipient subscription not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@PostMapping("/sms")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<CreateNotificationResponseDTO> sendSmsNotification(
				@Valid @RequestBody CreateNotificationRequestDTO request
		) {
			return notificationService.sendSmsNotification(request);
		}
		
		@Operation(
				summary = "Send an in-app notification",
				description = "Creates a persistent in-app notification for the recipient. No external dispatch — surfaced via polling or WebSocket. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "201", description = "In-app notification created"),
				@ApiResponse(responseCode = "400", description = "Invalid request payload"),
				@ApiResponse(responseCode = "403", description = "Recipient not subscribed to IN_APP channel"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@PostMapping("/in-app")
		@ResponseStatus(HttpStatus.CREATED)
		public ApiResponseConfig<CreateNotificationResponseDTO> sendInAppNotification(
				@Valid @RequestBody CreateNotificationRequestDTO request
		) {
			return notificationService.sendInAppNotification(request);
		}
		
		@Operation(
				summary = "Broadcast notification across all subscribed channels",
				description = "Sends the notification to every channel the recipient is subscribed to (EMAIL, SMS, IN_APP). Each channel is attempted independently. Requires ADMIN or SUPER_ADMIN role."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Broadcast completed"),
				@ApiResponse(responseCode = "404", description = "Recipient has no active subscription"),
				@ApiResponse(responseCode = "401", description = "Unauthorized"),
				@ApiResponse(responseCode = "403", description = "Forbidden")
		})
		@PostMapping("/broadcast")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<List<CreateNotificationResponseDTO>> broadcastNotification(
				@Valid @RequestBody CreateNotificationRequestDTO request
		) {
			return notificationService.broadcastNotification(request);
		}
		
		@Operation(
				summary = "Subscribe to notifications",
				description = "Registers a customer for notification delivery on one or more channels (EMAIL, SMS, IN_APP). If the recipient is already subscribed, their preferences are updated."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "201", description = "Subscribed successfully"),
				@ApiResponse(responseCode = "200", description = "Existing subscription updated"),
				@ApiResponse(responseCode = "400", description = "Invalid subscription payload"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@PostMapping("/subscribe")
		public ApiResponseConfig<SubscriptionResponseDTO> subscribe(
				@Valid @RequestBody SubscriptionRequestDTO request
		) {
			return notificationService.subscribe(request);
		}
		
		@Operation(
				summary = "Unsubscribe from all notifications",
				description = "Opts the recipient out of all notification channels by soft-deleting their subscription record."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Unsubscribed successfully"),
				@ApiResponse(responseCode = "404", description = "No active subscription found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@DeleteMapping("/unsubscribe/{recipientId}")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<Void> unsubscribe(
				@Parameter(description = "Recipient ID to unsubscribe", required = true)
				@PathVariable String recipientId
		) {
			return notificationService.unsubscribe(recipientId);
		}
		
		@Operation(
				summary = "Fetch a recipient's subscription preferences",
				description = "Returns the active subscription record for the given recipient, including subscribed channels and topics."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Subscription fetched successfully"),
				@ApiResponse(responseCode = "404", description = "No subscription found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@GetMapping("/subscription/{recipientId}")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<SubscriptionResponseDTO> fetchSubscription(
				@Parameter(description = "Recipient ID to look up", required = true)
				@PathVariable String recipientId
		) {
			return notificationService.fetchSubscription(recipientId);
		}
		
		@Operation(
				summary = "Fetch all notifications for a recipient",
				description = "Returns all notification records for the given recipient, sorted by newest first."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Notifications fetched successfully"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@GetMapping("/{recipientId}")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<List<CreateNotificationResponseDTO>> fetchNotificationsForRecipient(
				@Parameter(description = "Recipient ID", required = true)
				@PathVariable String recipientId
		) {
			return notificationService.fetchNotificationsForRecipient(recipientId);
		}
		
		@Operation(
				summary = "Mark a notification as read",
				description = "Sets the notification's read flag and records the readAt timestamp."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Notification marked as read"),
				@ApiResponse(responseCode = "404", description = "Notification not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@PatchMapping("/{notificationId}/read")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<CreateNotificationResponseDTO> markAsRead(
				@Parameter(description = "Notification ID to mark as read", required = true)
				@PathVariable String notificationId
		) {
			return notificationService.markAsRead(notificationId);
		}
		
		@Operation(
				summary = "Archive a notification",
				description = "Moves a notification to the recipient's archive. Archived notifications are hidden from the default notification feed."
		)
		@ApiResponses({
				@ApiResponse(responseCode = "200", description = "Notification archived successfully"),
				@ApiResponse(responseCode = "404", description = "Notification not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized")
		})
		@PatchMapping("/{notificationId}/archive")
		@ResponseStatus(HttpStatus.OK)
		public ApiResponseConfig<CreateNotificationResponseDTO> archiveNotification(
				@Parameter(description = "Notification ID to archive", required = true)
				@PathVariable String notificationId
		) {
			return notificationService.archiveNotification(notificationId);
		}
}