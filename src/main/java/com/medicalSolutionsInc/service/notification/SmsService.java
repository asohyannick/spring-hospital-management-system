package com.medicalSolutionsInc.service.notification;

import com.medicalSolutionsInc.config.twilioConfig.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {
		
		private final TwilioConfig twilioConfig;

		@Async
		public void sendSms(String toPhoneNumber, String messageBody) {
			try {
				Message message = Message.creator(
						new PhoneNumber(toPhoneNumber),
						new PhoneNumber(twilioConfig.getPhoneNumber()),
						messageBody
				).create();
				
				log.info("SMS sent to {} — SID: {}", toPhoneNumber, message.getSid());
				
			} catch (Exception e) {
				log.error("Failed to send SMS to {}: {}", toPhoneNumber, e.getMessage(), e);
				throw new RuntimeException("SMS delivery failed for: " + toPhoneNumber, e);
			}
		}
		
		public String formatSmsBody(String title, String message) {
			String full = "[" + title + "] " + message;
			return full.length() > 155 ? full.substring(0, 152) + "..." : full;
		}
}