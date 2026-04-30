package com.medicalSolutionsInc.config.rateLimitFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicalSolutionsInc.exceptions.globalExceptionResponseHandler.GlobalExceptionResponseHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;
	private final Map<String, List<Long>> requestCounts = new ConcurrentHashMap<>();
	private static final int MAX_REQUESTS = 300;
	private static final long WINDOW_MS = 60_000;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain chain) throws ServletException, IOException {
		String clientIp = request.getRemoteAddr();
		long now = System.currentTimeMillis();
		
		requestCounts.compute(clientIp, (ip, timestamps) -> {
			if (timestamps == null) timestamps = new ArrayList<>();
			timestamps.removeIf(t -> now - t > WINDOW_MS);
			timestamps.add(now);
			return timestamps;
		});
		
		if (requestCounts.get(clientIp).size() > MAX_REQUESTS) {
			response.setStatus(429);
			response.setContentType( MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding( StandardCharsets.UTF_8.name());
			
			GlobalExceptionResponseHandler error = new GlobalExceptionResponseHandler(
					Instant.now(),
					"Too Many Requests",
					"You have exceeded the " + MAX_REQUESTS + " requests per minute limit",
					429,
					"TOO_MANY_REQUESTS",
					request.getRequestURI(),
					request.getMethod()
			);
			
			response.getWriter().write(objectMapper.writeValueAsString(error));
			return;
		}
		
		chain.doFilter(request, response);
	}
}