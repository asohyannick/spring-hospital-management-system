package com.medicalSolutionsInc.config.twilioConfig;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

@Slf4j
@Getter
@Configuration
public class TwilioConfig {

		@Value("${twilio.account-sid}")
		private String accountSid;
		
		@Value("${twilio.auth-token}")
		private String authToken;
		
		@Value("${twilio.phone-number}")
		private String phoneNumber;

		@PostConstruct
		public void initTwilio() {
			log.info("Twilio SID: {}", accountSid.substring(0, 6) + "...");
			log.info("Twilio Auth Token present: {}", authToken != null && !authToken.isBlank());
			Twilio.init(accountSid, authToken);
			log.info("Twilio client initialised — sending from: {}", phoneNumber);
		}
}