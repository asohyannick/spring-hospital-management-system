package com.medicalSolutionsInc.controller.chatbotController;

import com.medicalSolutionsInc.dto.chatDTO.ChatRequestDTO;
import com.medicalSolutionsInc.dto.chatDTO.ChatResponseDTO;
import com.medicalSolutionsInc.dto.chatDTO.ImageGenerationRequestDTO;
import com.medicalSolutionsInc.dto.chatDTO.ImageGenerationResponseDTO;
import com.medicalSolutionsInc.service.chatbotService.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping ("/api/${api.version}/chatbot")
@RequiredArgsConstructor
@Tag (name = "AI Chatbot", description = "Health assistant powered by Claude AI")
public class ChatbotController {

		private final ChatbotService chatbotService;

		@PostMapping("/chat")
		@Operation(summary = "Send a message to the AI health assistant")
		public ResponseEntity<ChatResponseDTO> chat(
				@RequestBody @Valid ChatRequestDTO request,
				Authentication authentication
		) {
			String role = authentication.getAuthorities()
					              .stream()
					              .findFirst()
					              .map( GrantedAuthority::getAuthority)
					              .orElse("UNKNOWN");
			
			ChatResponseDTO response = chatbotService.chat(request.message(), role);
			return ResponseEntity.ok(response);
		}

		@PostMapping("/generate-image")
		@Operation(summary = "Generate an image from a text prompt")
		public ResponseEntity< ImageGenerationResponseDTO > generateImage(
				@RequestBody @Valid ImageGenerationRequestDTO request,
				Authentication authentication
		) {
			ImageGenerationResponseDTO response = chatbotService.generateImage(request);
			return ResponseEntity.ok(response);
		}
}