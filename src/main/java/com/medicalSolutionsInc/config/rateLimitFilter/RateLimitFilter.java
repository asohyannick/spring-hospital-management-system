package com.medicalSolutionsInc.config.rateLimitFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order (1)
public class RateLimitFilter extends OncePerRequestFilter {

		private final Map <String, List <Long> > requestCounts = new ConcurrentHashMap <> ();
		private static final int MAX_REQUESTS = 100;
		private static final long WINDOW_MS = 60_000;
		
		@Override
		protected void doFilterInternal( HttpServletRequest request,
		                                 HttpServletResponse response,
		                                 FilterChain chain) throws ServletException, IOException {
			String clientIp = request.getRemoteAddr();
			long now = System.currentTimeMillis();
			
			requestCounts.compute(clientIp, (ip, timestamps) -> {
				if (timestamps == null) timestamps = new ArrayList <> ();
				timestamps.removeIf(t -> now - t > WINDOW_MS);
				timestamps.add(now);
				return timestamps;
			});
			
			if (requestCounts.get(clientIp).size() > MAX_REQUESTS) {
				response.setStatus(429);
				response.getWriter().write("{\"message\":\"Too many requests\",\"statusCode\":429}");
				return;
			}
			
			chain.doFilter(request, response);
		}
}