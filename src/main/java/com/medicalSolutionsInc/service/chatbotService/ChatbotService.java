package com.medicalSolutionsInc.service.chatbotService;

import com.medicalSolutionsInc.dto.chatDTO.ChatResponseDTO;
import com.medicalSolutionsInc.dto.chatDTO.ImageGenerationRequestDTO;
import com.medicalSolutionsInc.dto.chatDTO.ImageGenerationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {

private final WebClient groqWebClient;

		@Value("${groq.model}")
		private String model;
		
		@Value("${groq.max-tokens}")
		private int maxTokens;
		
		private static final String SYSTEM_PROMPT = """
		            You are a knowledgeable medical assistant for a hospital management system.
		            You assist patients, doctors, nurses, and staff with health-related questions,
		            medical information, appointment guidance, medication queries, and general wellness advice.
		            
		            Important rules:
		            - Always recommend consulting a qualified doctor for diagnosis or treatment decisions
		            - Never prescribe medications or provide dosage instructions as medical advice
		            - Be empathetic, clear, and professional in all responses
		            - If a question is outside medical/health scope, answer helpfully but briefly
		            - For emergencies, always direct users to call emergency services immediately
		            """;
		
		public ChatResponseDTO chat(String userMessage, String role) {
			log.info("Chatbot request from role: {}", role);
			
			Map<String, Object> requestBody = Map.of(
					"model", model,
					"max_tokens", maxTokens,
					"messages", List.of(
							Map.of("role", "system", "content", SYSTEM_PROMPT),
							Map.of("role", "user", "content", userMessage)
					)
			);
			
			Map response = groqWebClient.post()
					               .uri("/chat/completions")
					               .bodyValue(requestBody)
					               .retrieve()
					               .bodyToMono(Map.class)
					               .block();
			
			String reply = extractReply(response);
			
			return new ChatResponseDTO(
					UUID.randomUUID().toString(),
					reply,
					Instant.now()
			);
		}

		public ImageGenerationResponseDTO generateImage(ImageGenerationRequestDTO request) {
			log.info("Generating image for prompt: '{}'", request.prompt());
			
			String encodedPrompt = UriUtils.encode(request.prompt(), StandardCharsets.UTF_8);
			int width  = request.width()  > 0 ? request.width()  : 512;
			int height = request.height() > 0 ? request.height() : 512;
			String model = request.model() != null ? request.model() : "flux";
			
			String imageUrl = String.format(
					"https://image.pollinations.ai/prompt/%s?width=%d&height=%d&model=%s&nologo=true",
					encodedPrompt, width, height, model
			);
			
			log.info("Image URL generated: {}", imageUrl);
			
			return new ImageGenerationResponseDTO(
					UUID.randomUUID().toString(),
					request.prompt(),
					imageUrl,
					Instant.now()
			);
		}
		
		private String extractReply(Map response) {
			List<Map> choices = (List<Map>) response.get("choices");
			Map message = (Map) choices.get(0).get("message");
			return (String) message.get("content");
		}
}