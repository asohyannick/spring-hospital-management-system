package com.medicalSolutionsInc.config.cookieConfig;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@Slf4j
public class CookieConfig {

		private static final String ACCESS_TOKEN_COOKIE  = "access_token";
		private static final String REFRESH_TOKEN_COOKIE = "refresh_token";
		private static final int    ACCESS_TOKEN_MAX_AGE  = 60 * 60;
		private static final int    REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;
		
		public void setAccessTokenCookie(HttpServletResponse response, String token) {
			Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE, token);
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			cookie.setPath("/");
			cookie.setMaxAge(ACCESS_TOKEN_MAX_AGE);
			cookie.setAttribute("SameSite", "Strict");
			response.addCookie(cookie);
		}
		
		public void setRefreshTokenCookie(HttpServletResponse response, String token) {
			Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, token);
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			cookie.setPath("/");
			cookie.setMaxAge(REFRESH_TOKEN_MAX_AGE);
			cookie.setAttribute("SameSite", "Strict");
			response.addCookie(cookie);
		}
		
		public void clearTokenCookies(HttpServletResponse response) {
			Cookie accessCookie = new Cookie(ACCESS_TOKEN_COOKIE, "");
			accessCookie.setHttpOnly(true);
			accessCookie.setSecure(true);
			accessCookie.setPath("/");
			accessCookie.setMaxAge(0);
			response.addCookie(accessCookie);
			
			Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE, "");
			refreshCookie.setHttpOnly(true);
			refreshCookie.setSecure(true);
			refreshCookie.setPath("/");
			refreshCookie.setMaxAge(0);
			response.addCookie(refreshCookie);
		}
		
		public String extractAccessToken(HttpServletRequest request) {
			if (request.getCookies() == null) return null;
			return Arrays.stream(request.getCookies())
					       .filter(c -> ACCESS_TOKEN_COOKIE.equals(c.getName()))
					       .map(Cookie::getValue)
					       .findFirst()
					       .orElse(null);
		}
}