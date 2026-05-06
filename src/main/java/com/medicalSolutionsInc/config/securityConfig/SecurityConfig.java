package com.medicalSolutionsInc.config.securityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicalSolutionsInc.config.corConfig.CorsConfiguration;
import com.medicalSolutionsInc.config.firebaseConfig.FirebaseAuthenticationFilter;
import com.medicalSolutionsInc.config.jwtConfig.JWTAuthenticationFilter;
import com.medicalSolutionsInc.config.rateLimitFilter.RateLimitFilter;
import com.medicalSolutionsInc.exceptions.globalExceptionResponseHandler.GlobalExceptionResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;
        private final JWTAuthenticationFilter jwtAuthenticationFilter;
		private final CorsConfiguration corsConfig;
		private final ObjectMapper objectMapper;
        private final RateLimitFilter rateLimitFilter;
		
        @Value ( "${api.version}" )
		private String apiVersion;
		
		@Value("${github.client-id}")
		private String githubClientId;
		
		@Value("${github.client-secret}")
		private String githubClientSecret;
		
		@Value("${github.redirect-uri}")
		private String githubRedirectUri;
		
		private String[] publicEndpoints ( ) {
			String base = "/api/" + apiVersion + "/users";
			return new String[] {
					base + "/auth/register",
					base + "/auth/login",
					base + "/auth/logout",
					base + "/auth/google-login",
					base + "/auth/github/callback",
					base + "/auth/github-login",
					base + "/auth/magic-link/send",
					base + "/auth/magic-link/verify",
					base + "/auth/magic-link/resend",
					base + "/auth/forgot-password",
					base + "/auth/reset-password",
					base + "/auth/create-password",
					"/v3/api-docs",
					"/v3/api-docs/**",
					"/swagger-ui/**",
					"/swagger-ui.html",
					"/swagger-resources/**",
					"/webjars/**",
					"/error"
			};
		}
		
		@Bean
		public BCryptPasswordEncoder passwordEncoder ( ) {
			return new BCryptPasswordEncoder ( );
		}
		
		@Bean
		public AuthenticationManager authenticationManager (
				AuthenticationConfiguration authenticationConfiguration
		) throws Exception {
			return authenticationConfiguration.getAuthenticationManager ( );
		}

		@Bean
		public ClientRegistrationRepository clientRegistrationRepository() {
			return new InMemoryClientRegistrationRepository (githubClientRegistration());
		}
		
		@Bean
		public OAuth2AuthorizedClientRepository authorizedClientRepository() {
			return new HttpSessionOAuth2AuthorizedClientRepository ();
		}
		
		@Bean
		public AccessDeniedHandler accessDeniedHandler ( ) {
			return ( request , response , accessDeniedException ) -> {
				log.warn ( "Access denied for [{}] {}: {}" ,
						request.getMethod ( ) ,
						request.getRequestURI ( ) ,
						accessDeniedException.getMessage ( )
				);
				
				response.setStatus ( HttpStatus.FORBIDDEN.value ( ) );
				response.setContentType ( MediaType.APPLICATION_JSON_VALUE );
				response.setCharacterEncoding ( StandardCharsets.UTF_8.name ( ) );
				
				GlobalExceptionResponseHandler error = new GlobalExceptionResponseHandler (
						Instant.now ( ) ,
						"Access denied" ,
						"You do not have permission to access this resource" ,
						HttpStatus.FORBIDDEN.value ( ) ,
						"FORBIDDEN" ,
						request.getRequestURI ( ) ,
						request.getMethod ( )
				);
				
				response.getWriter ( ).write ( objectMapper.writeValueAsString ( error ) );
			};
		}
		
		@Bean
		public AuthenticationEntryPoint authenticationEntryPoint ( ) {
			return ( request , response , authException ) -> {
				log.warn ( "Unauthorized access for [{}] {}: {}" ,
						request.getMethod ( ) ,
						request.getRequestURI ( ) ,
						authException.getMessage ( )
				);
				
				response.setStatus ( HttpStatus.UNAUTHORIZED.value ( ) );
				response.setContentType ( MediaType.APPLICATION_JSON_VALUE );
				response.setCharacterEncoding ( StandardCharsets.UTF_8.name ( ) );
				
				GlobalExceptionResponseHandler error = new GlobalExceptionResponseHandler (
						Instant.now ( ) ,
						"Unauthorized" ,
						"Authentication is required to access this resource" ,
						HttpStatus.UNAUTHORIZED.value ( ) ,
						"UNAUTHORIZED" ,
						request.getRequestURI ( ) ,
						request.getMethod ( )
				);
				
				response.getWriter ( ).write ( objectMapper.writeValueAsString ( error ) );
			};
		}

		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			http
					.cors(cors -> cors
							              .configurationSource(corsConfig.corsConfigurationSource())
					)
					.csrf(AbstractHttpConfigurer::disable)
					.sessionManagement(session -> session
							                              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					)
					
					.oauth2Client(oauth2 -> oauth2
							                        .clientRegistrationRepository(clientRegistrationRepository())
							                        .authorizedClientRepository(authorizedClientRepository())
					)
					.addFilterBefore(rateLimitFilter, ChannelProcessingFilter.class)
					.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
					.authorizeHttpRequests(auth -> auth
														   
							                               .requestMatchers("/actuator/health", "/actuator/info", "/actuator/metrics", "/actuator/loggers", "/actuator/beans")
							                               .hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers("/actuator/**").hasRole("SUPER_ADMIN")
														   
							                               .requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**").permitAll()
							                               .requestMatchers(publicEndpoints()).permitAll()
														   
							                               // ─── Authentication and Authorization Management endpoints ─────────────────────────────────────────────────────
							                               .requestMatchers ( HttpMethod.GET , "/api/" + apiVersion + "/users" ).hasAnyRole ( "ADMIN" , "SUPER_ADMIN" )
							                               .requestMatchers ( HttpMethod.GET , "/api/" + apiVersion + "/users/*" ).hasAnyRole ( "ADMIN" , "SUPER_ADMIN" )
							                               .requestMatchers ( HttpMethod.PATCH , "/api/" + apiVersion + "/auth/users/*/block" ).hasAnyRole ( "ADMIN" , "SUPER_ADMIN" )
							                               .requestMatchers ( HttpMethod.PATCH , "/api/" + apiVersion + "/auth/users/*/unblock" ).hasAnyRole ( "ADMIN" , "SUPER_ADMIN")
							                               
							                               // ─── Booking Management ────────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/booking/add-booking").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/booking").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/booking/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/booking/search").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/booking/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.PUT,    "/api/" + apiVersion + "/booking/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/booking/*").hasRole("SUPER_ADMIN")
							                               
							                               // ─── Laboratory Management ─────────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/lab").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/lab").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/lab/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/lab/search").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/lab/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/lab/*").hasRole("SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.PUT, "/api/" + apiVersion + "/lab/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               
							                               // ─── Medical Record Management ─────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/medical-record").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/medical-record").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/medical-record/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/medical-record/search").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/medical-record/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.PUT,    "/api/" + apiVersion + "/medical-record/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/medical-record/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               
							                               // ─── Patient Profile Management ────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/patient").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/patient").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/patient/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/patient/search").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/patient/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.PUT,    "/api/" + apiVersion + "/patient/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/patient/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               
							                               // ─── Pharmacy Management ───────────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/pharmacy").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/pharmacy").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE", "PHARMACIST")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/pharmacy/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/pharmacy/search").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE", "PHARMACIST")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/pharmacy/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE", "PHARMACIST")
							                               .requestMatchers(HttpMethod.PUT,    "/api/" + apiVersion + "/pharmacy/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/pharmacy/*").hasRole("SUPER_ADMIN")
							                               
							                               // ─── Review Management ─────────────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/review").hasAnyRole("ADMIN", "SUPER_ADMIN", "PATIENT")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/review").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/review/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/review/search").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/review/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE", "PATIENT")
							                               .requestMatchers(HttpMethod.PUT,    "/api/" + apiVersion + "/review/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "PATIENT")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/review/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
														   
							                               // ─── Staff Profile Management ──────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/staff-profile").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/staff-profile").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/staff-profile/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/staff-profile/search").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/staff-profile/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.PUT,    "/api/" + apiVersion + "/staff-profile/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/staff-profile/*").hasRole("SUPER_ADMIN")
							                               
							                               // ─── Ward Management ──────────────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/ward").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/ward").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/ward/count").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/ward/search").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/ward/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.PUT,    "/api/" + apiVersion + "/ward/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/ward/*").hasRole("SUPER_ADMIN")
							                               
							                               // ─── Notification Management ──────────────────────────────────────────────
							                               // Send endpoints — ADMIN / SUPER_ADMIN only
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/notify/email").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/notify/sms").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/notify/in-app").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/notify/broadcast").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               // Subscription management — any authenticated user (self-service)
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/notify/subscribe").authenticated()
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/notify/unsubscribe/*").authenticated()
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/notify/subscription/*").authenticated()
							                               // Recipient notification feed — authenticated users (own feed)
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/notify/*").authenticated()
							                               .requestMatchers(HttpMethod.PATCH,  "/api/" + apiVersion + "/notify/*/read").authenticated()
							                               .requestMatchers(HttpMethod.PATCH,  "/api/" + apiVersion + "/notify/*/archive").authenticated()
							                               
							                               // ─── Payment Management ────────────────────────────────────────────────────
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/payment/checkout").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE", "PATIENT")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/payment").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE")
							                               .requestMatchers(HttpMethod.GET,    "/api/" + apiVersion + "/payment/*").hasAnyRole("ADMIN", "SUPER_ADMIN", "DOCTOR", "NURSE", "PATIENT")
							                               .requestMatchers(HttpMethod.DELETE, "/api/" + apiVersion + "/payment/*").hasAnyRole("ADMIN", "SUPER_ADMIN")
							                               .requestMatchers(HttpMethod.POST,   "/api/" + apiVersion + "/payment/*/refund").hasAnyRole("ADMIN", "SUPER_ADMIN")
														   
							                               .anyRequest().authenticated ()
					)
					.exceptionHandling(exception -> exception
							                                .accessDeniedHandler(accessDeniedHandler())
							                                .authenticationEntryPoint(authenticationEntryPoint())
					)
					.addFilterBefore ( firebaseAuthenticationFilter , UsernamePasswordAuthenticationFilter.class )
					.addFilterBefore ( jwtAuthenticationFilter , FirebaseAuthenticationFilter.class );
			
					return http.build();
				}
		
		private ClientRegistration githubClientRegistration() {
			return CommonOAuth2Provider.GITHUB
					       .getBuilder("github")
					       .clientId(githubClientId)
					       .clientSecret(githubClientSecret)
					       .redirectUri(githubRedirectUri)
					       .build();
		}
}