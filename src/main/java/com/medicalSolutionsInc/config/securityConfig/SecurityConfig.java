package com.medicalSolutionsInc.config.securityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicalSolutionsInc.config.corConfig.CorsConfiguration;
import com.medicalSolutionsInc.config.firebaseConfig.FirebaseAuthenticationFilter;
import com.medicalSolutionsInc.config.jwtConfig.JWTAuthenticationFilter;
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
		@Value ( "${api.version}" )
		private String apiVersion;
		
		@Value("${github.client-id}")
		private String githubClientId;
		
		@Value("${github.client-secret}")
		private String githubClientSecret;
		
		@Value("${github.redirect-uri}")
		private String githubRedirectUri;
		
		private String[] publicEndpoints ( ) {
			String base = "/api/" + apiVersion + "/auth";
			return new String[] {
					base + "/auth/register",
					base + "/auth/login",
					base + "/auth/logout",
					base + "/auth/google-login",
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
					.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
					.authorizeHttpRequests(auth -> auth
							                               .requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**").permitAll()
							                               .requestMatchers(publicEndpoints()).permitAll()
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