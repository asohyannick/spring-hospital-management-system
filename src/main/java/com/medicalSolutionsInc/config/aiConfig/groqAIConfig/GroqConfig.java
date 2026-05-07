package com.medicalSolutionsInc.config.aiConfig.groqAIConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GroqConfig {
		
		@Value("${groq.api-key}")
		private String apiKey;
		
		@Bean
		public WebClient groqWebClient() {
			return WebClient.builder()
					       .baseUrl("https://api.groq.com/openai/v1")
					       .defaultHeader("Authorization", "Bearer " + apiKey)
					       .defaultHeader("Content-Type", "application/json")
					       .build();
		}
}