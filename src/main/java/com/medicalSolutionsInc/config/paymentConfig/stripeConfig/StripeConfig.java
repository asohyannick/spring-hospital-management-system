package com.medicalSolutionsInc.config.paymentConfig.stripeConfig;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.stripe.Stripe;
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeConfig {
	
	@Value ("${stripe.api-key}")
	private String apiKey;
	
	@PostConstruct
	public void init() {
		Stripe.apiKey = this.apiKey;
	}
}