package com.medicalSolutionsInc.service.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.medicalSolutionsInc.config.emailTemplateConfig.EmailTemplateConfig;
import com.medicalSolutionsInc.config.jwtConfig.JWTConfiguration;
import com.medicalSolutionsInc.dto.userDTO.*;
import com.medicalSolutionsInc.entity.user.User;
import com.medicalSolutionsInc.enumerations.user.UserRole;
import com.medicalSolutionsInc.exceptions.badRequestException.BadRequestException;
import com.medicalSolutionsInc.exceptions.conflictRequestException.ConflictRequestException;
import com.medicalSolutionsInc.exceptions.notFoundException.NotFoundException;
import com.medicalSolutionsInc.exceptions.unAuthorizedException.UnAuthorizedException;
import com.medicalSolutionsInc.mappers.userMapper.auth.UserMapper;
import com.medicalSolutionsInc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

		private final UserRepository userRepository;
		private final PasswordEncoder passwordEncoder;
		private final UserMapper userMapper;
        private final FirebaseAuth firebaseAuth;
        private final EmailTemplateConfig emailTemplateConfig;
		private final JWTConfiguration jwtConfiguration;
		
		public UserResponseDTO registerUser(RegistrationRequestDTO request) throws Exception {
			if (userRepository.existsByEmail(request.email())) {
				throw new ConflictRequestException ("An account with this email already exists");
			}
			
			User user = userMapper.toEntity(request);
			user.setAccountVerified(false);
			user.setAccountActive(false);
			user.setActive(true);
			user.setAccountBlocked(false);
			user.setFailedLoginAttempts(0);
			user.setTwoFactorAttempts(0);
			user.setCreatedAt(Instant.now());
			
			String setupToken = jwtConfiguration.generateAccountSetupToken(request.email());
			user.setMagicLinkToken(setupToken);
			user.setMagicLinkTokenExpiration(
					Instant.now().plus(15, java.time.temporal.ChronoUnit.MINUTES).toString()
			);
			
			User savedUser = userRepository.save(user);
			
			emailTemplateConfig.sendAccountCreatedEmail(
					savedUser.getEmail(),
					savedUser.getFirstName(),
					setupToken
			);
			
			log.info("User registered successfully: {}", savedUser.getEmail());
			return userMapper.toResponse(savedUser);
		}
		
		public UserResponseDTO createPassword( String token, CreatePasswordRequestDTO createPasswordRequestDTO) throws  Exception {
			if (!createPasswordRequestDTO.equals(createPasswordRequestDTO.confirmPassword ())) {
				throw new BadRequestException("Passwords do not match");
			}
			
			if (!jwtConfiguration.isAccountSetupToken(token)) {
				throw new BadRequestException("Invalid or expired account setup token");
			}
			
			String email = jwtConfiguration.extractEmail(token);
			User user = findUserByEmail(email);
			
			if (user.getAccountVerified()) {
				throw new BadRequestException("Account has already been activated");
			}
			
			user.setPassword(passwordEncoder.encode(createPasswordRequestDTO.password ()));
			user.setAccountVerified(true);
			user.setAccountActive(true);
			user.setMagicLinkToken(null);
			user.setMagicLinkTokenExpiration(null);
			user.setUpdatedAt(Instant.now());
			
			log.info("Password created and account activated for: {}", email);
			return userMapper.toResponse(userRepository.save(user));
		}
		
		public UserResponseDTO login(LoginRequestDTO request) throws  Exception{
			User user = findUserByEmail(request.email());
			
			if (user.isAccountBlocked()) {
				throw new UnAuthorizedException ("Your account has been blocked. Please contact support");
			}
			
			if (!user.getAccountActive()) {
				throw new UnAuthorizedException ("Your account is not yet activated. Please check your email");
			}
			
			if (!passwordEncoder.matches(request.password(), user.getPassword())) {
				user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
				
				if (user.getFailedLoginAttempts() >= 5) {
					user.setAccountBlocked(true);
					userRepository.save(user);
					throw new UnAuthorizedException ("Account blocked due to too many failed login attempts");
				}
				
				userRepository.save(user);
				throw new UnAuthorizedException ("Invalid email or password");
			}
			
			user.setFailedLoginAttempts(0);
			String accessToken  = jwtConfiguration.generateAccessToken(user.getEmail(), user.getRole().name());
			String refreshToken = jwtConfiguration.generateRefreshToken(user.getEmail());
			
			user.setAccessToken(accessToken);
			user.setRefreshToken(refreshToken);
			user.setUpdatedAt(Instant.now());
			
			log.info("User logged in successfully: {}", user.getEmail());
			return userMapper.toResponse(userRepository.save(user));
		}
		
		
		public void logout(String token) throws Exception {
			String email = jwtConfiguration.extractEmail(token);
			User user = findUserByEmail(email);
			
			user.setAccessToken(null);
			user.setRefreshToken(null);
			user.setUpdatedAt(Instant.now());
			
			userRepository.save(user);
			log.info("User logged out successfully: {}", email);
		}
		
		@Transactional(readOnly = true)
		public List<UserResponseDTO> fetchAllUsers() {
			return userMapper.toResponseList(userRepository.findAll());
		}
		
		public void deleteUserAccount(String userId) throws  Exception {
			User user = userRepository.findById(userId)
					            .orElseThrow(() -> new NotFoundException ("User not found with id: " + userId));
			
			userRepository.delete(user);
			log.info("User account deleted: {}", user.getEmail());
		}
		
		public void forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO) throws Exception {
			User user = findUserByEmail(forgotPasswordRequestDTO.email ());
			
			if (user.isAccountBlocked()) {
				throw new UnAuthorizedException ("Your account has been blocked. Please contact support");
			}
			
			String resetToken = jwtConfiguration.generatePasswordResetToken(forgotPasswordRequestDTO.email ());
			user.setMagicLinkToken(resetToken);
			user.setMagicLinkTokenExpiration(
					Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES).toString()
			);
			userRepository.save(user);
			
			emailTemplateConfig.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), resetToken);
			log.info("Password reset email sent to: {}", forgotPasswordRequestDTO.email ());
		}
		
		public void resetPassword( String token, ResetPasswordRequestDTO resetPasswordRequestDTO ) throws Exception {
			if (!resetPasswordRequestDTO.equals(resetPasswordRequestDTO.confirmPassword ())) {
				throw new BadRequestException("Passwords do not match");
			}
			
			if (!jwtConfiguration.isPasswordResetToken(token)) {
				throw new BadRequestException("Invalid or expired password reset token");
			}
			
			String email = jwtConfiguration.extractEmail(token);
			User user = findUserByEmail(email);
			
			user.setPassword(passwordEncoder.encode( resetPasswordRequestDTO.newPassword ()));
			user.setMagicLinkToken(null);
			user.setMagicLinkTokenExpiration(null);
			user.setFailedLoginAttempts(0);
			user.setUpdatedAt(Instant.now());
			
			userRepository.save(user);
			log.info("Password reset successfully for: {}", email);
		}
		
		public void sendMagicLink(MagicLinkTokenRequestDTO magicLinkTokenRequestDTO) throws Exception {
			User user = findUserByEmail(magicLinkTokenRequestDTO.email ());
			
			if (user.isAccountBlocked()) {
				throw new UnAuthorizedException ("Your account has been blocked. Please contact support");
			}
			
			if (!user.getAccountActive()) {
				throw new UnAuthorizedException ("Please activate your account first");
			}
			
			String magicToken = jwtConfiguration.generateMagicLinkToken(magicLinkTokenRequestDTO.email ());
			user.setMagicLinkToken(magicToken);
			user.setMagicLinkTokenExpiration(
					Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES).toString()
			);
			userRepository.save(user);
			
			emailTemplateConfig.sendMagicLinkEmail(user.getEmail(), user.getFirstName(), magicToken);
			log.info("Magic link sent to: {}", magicLinkTokenRequestDTO.email ());
		}
		
		public UserResponseDTO verifyMagicLink(VerifyMagicLinkTokenDTO verifyMagicLinkTokenDTO) throws Exception {
			if (!jwtConfiguration.isMagicLinkToken(verifyMagicLinkTokenDTO.token ())) {
				throw new BadRequestException("Invalid or expired magic link token");
			}
			
			String email = jwtConfiguration.extractEmail(verifyMagicLinkTokenDTO.token ());
			User user = findUserByEmail(email);
			
			if (!verifyMagicLinkTokenDTO.equals(user.getMagicLinkToken())) {
				throw new BadRequestException("Magic link token mismatch or already used");
			}
			
			String accessToken  = jwtConfiguration.generateAccessToken(user.getEmail(), user.getRole().name());
			String refreshToken = jwtConfiguration.generateRefreshToken(user.getEmail());
			
			user.setAccessToken(accessToken);
			user.setRefreshToken(refreshToken);
			user.setMagicLinkToken(null);
			user.setMagicLinkTokenExpiration(null);
			user.setUpdatedAt(Instant.now());
			
			log.info("Magic link verified and user logged in: {}", email);
			return userMapper.toResponse(userRepository.save(user));
		}
		
		public void resendMagicLink(MagicLinkTokenRequestDTO magicLinkTokenRequestDTO) throws Exception {
			sendMagicLink(magicLinkTokenRequestDTO);
			log.info("Magic link resent to: {}", magicLinkTokenRequestDTO.email ());
		}
		
		public UserResponseDTO blockUser(String userId) throws Exception {
			User user = userRepository.findById(userId)
					            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
			
			if (user.isAccountBlocked()) {
				throw new BadRequestException("User account is already blocked");
			}
			
			user.setAccountBlocked(true);
			user.setActive(false);
			user.setAccessToken(null);
			user.setRefreshToken(null);
			user.setUpdatedAt(Instant.now());
			
			log.info("User account blocked: {}", user.getEmail());
			return userMapper.toResponse(userRepository.save(user));
		}
		
		public UserResponseDTO unblockUser(String userId) throws Exception {
			User user = userRepository.findById(userId)
					            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
			
			if (!user.isAccountBlocked()) {
				throw new BadRequestException("User account is not blocked");
			}
			
			user.setAccountBlocked(false);
			user.setActive(true);
			user.setFailedLoginAttempts(0);
			user.setUpdatedAt(Instant.now());
			
			log.info("User account unblocked: {}", user.getEmail());
			return userMapper.toResponse(userRepository.save(user));
		}

		public UserResponseDTO loginViaFirebase(FirebaseLoginRequestDTO firebaseLoginRequestDTO) throws Exception {
			FirebaseToken decodedToken = firebaseAuth.verifyIdToken(firebaseLoginRequestDTO.idToken ());
			String email = decodedToken.getEmail();
			
			User user = userRepository.findByEmail(email).orElseGet(() -> {
				User newUser = User.builder()
						               .email(email)
						               .role(UserRole.DEVOPS_ENGINEER)
						               .accountVerified(true)
						               .accountActive(true)
						               .active(true)
						               .accountBlocked(false)
						               .failedLoginAttempts(0)
						               .twoFactorAttempts(0)
						               .createdAt(Instant.now())
						               .build();
				return userRepository.save(newUser);
			});
			
			if (user.isAccountBlocked()) {
				throw new UnAuthorizedException("Your account has been blocked. Please contact support");
			}
			
			String accessToken  = jwtConfiguration.generateAccessToken(user.getEmail(), user.getRole().name());
			String refreshToken = jwtConfiguration.generateRefreshToken(user.getEmail());
			
			user.setAccessToken(accessToken);
			user.setRefreshToken(refreshToken);
			user.setUpdatedAt(Instant.now());
			
			log.info("User logged in via Firebase: {}", email);
			return userMapper.toResponse(userRepository.save(user));
		}
		
		public UserResponseDTO loginViaGithub(String githubEmail, String githubName, String avatarUrl) throws Exception {
			User user = userRepository.findByEmail(githubEmail).orElseGet(() -> {
				String[] nameParts = githubName != null ? githubName.split(" ", 2) : new String[]{"", ""};
				User newUser = User.builder()
						               .email(githubEmail)
						               .firstName(nameParts[0])
						               .lastName(nameParts.length > 1 ? nameParts[1] : "")
						               .avatarUrl(avatarUrl)
						               .role(UserRole.DEVOPS_ENGINEER)
						               .accountVerified(true)
						               .accountActive(true)
						               .active(true)
						               .accountBlocked(false)
						               .failedLoginAttempts(0)
						               .twoFactorAttempts(0)
						               .createdAt(Instant.now())
						               .build();
				return userRepository.save(newUser);
			});
			
			if (user.isAccountBlocked()) {
				throw new UnAuthorizedException ("Your account has been blocked. Please contact support");
			}
			
			String accessToken  = jwtConfiguration.generateAccessToken(user.getEmail(), user.getRole().name());
			String refreshToken = jwtConfiguration.generateRefreshToken(user.getEmail());
			
			user.setAccessToken(accessToken);
			user.setRefreshToken(refreshToken);
			user.setUpdatedAt(Instant.now());
			
			log.info("User logged in via GitHub: {}", githubEmail);
			return userMapper.toResponse(userRepository.save(user));
		}
		
		private User findUserByEmail(String email) throws Exception {
			return userRepository.findByEmail(email)
					       .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
		}
}