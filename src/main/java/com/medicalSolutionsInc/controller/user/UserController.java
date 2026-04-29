package com.medicalSolutionsInc.controller.user;

import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.userDTO.*;
import com.medicalSolutionsInc.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@Tag(name = "Authentication & User Management Endpoints", description = "APIs for authentication and user management")
@RequestMapping("/api/${api.version}/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@Operation(summary = "Register a new user", description = "Creates a new user account and sends an account setup email with a token")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "User registered successfully"),
			@ApiResponse(responseCode = "409", description = "Email already in use")
	})
	@PostMapping("/auth/register")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> register(
			@Valid @RequestBody RegistrationRequestDTO request
	) throws Exception {
		UserResponseDTO user = userService.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(
				new ApiResponseConfig<>(
						Instant.now(),
						"User registered successfully. Please check your email to set up your password.",
						user,
						HttpStatus.CREATED.value()
				)
		);
	}

	@Operation(summary = "Create password", description = "Activates the user account by setting a password using the token from the setup email")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Password created and account activated"),
			@ApiResponse(responseCode = "400", description = "Invalid token or passwords do not match")
	})
	@PostMapping("/auth/create-password")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> createPassword(
			@Parameter(description = "Account setup token from email")
			@RequestParam String token,
			
			@Parameter(description = "New password and confirm password")
			@RequestBody CreatePasswordRequestDTO createPasswordRequestDTO
	
	) throws Exception {
		UserResponseDTO user = userService.createPassword(token, createPasswordRequestDTO);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Password created successfully. Your account is now active.",
						user,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Login", description = "Authenticates a user with email and password and returns access and refresh tokens")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Login successful"),
			@ApiResponse(responseCode = "401", description = "Invalid credentials or account blocked")
	})
	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> login(
			@Valid @RequestBody LoginRequestDTO request
	) throws Exception{
		UserResponseDTO user = userService.login(request);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Login successful. Welcome back!",
						user,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Logout", description = "Invalidates the user's access and refresh tokens")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Logout successful"),
			@ApiResponse(responseCode = "401", description = "Unauthorized")
	})
	@PostMapping("/auth/logout")
	public ResponseEntity<ApiResponseConfig<Void>> logout(
			@Parameter(description = "Bearer access token")
			@RequestHeader("Authorization") String authHeader
	) throws Exception {
		String token = authHeader.replace("Bearer ", "");
		userService.logout(token);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Logged out successfully.",
						null,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Fetch all users", description = "Returns a list of all registered users")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Users fetched successfully"),
			@ApiResponse(responseCode = "403", description = "Access denied")
	})
	@GetMapping
	public ResponseEntity<ApiResponseConfig<List<UserResponseDTO>>> fetchAllUsers() {
		List<UserResponseDTO> users = userService.fetchAllUsers();
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Users fetched successfully.",
						users,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Delete user account", description = "Permanently deletes a user account by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "User deleted successfully"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseConfig<Void>> deleteUserAccount(
			@Parameter(description = "User ID") @PathVariable String userId
	) throws Exception {
		userService.deleteUserAccount(userId);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"User account deleted successfully.",
						null,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Forgot password", description = "Sends a password reset link to the user's email")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Password reset email sent"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PostMapping("/auth/forgot-password")
	public ResponseEntity<ApiResponseConfig<Void>> forgotPassword(
			@Parameter(description = "User email address")
			@Valid @RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO
	) throws Exception {
		userService.forgotPassword(forgotPasswordRequestDTO);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Password reset email sent. Please check your inbox.",
						null,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Reset password", description = "Resets the user's password using the token from the reset email")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Password reset successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid token or passwords do not match")
	})
	@PostMapping("/auth/reset-password")
	public ResponseEntity<ApiResponseConfig<Void>> resetPassword(
			@Parameter(description = "Password reset token from email")
			@RequestParam String token,
			@Parameter(description = "Reset password for users")
			@Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO
	) throws Exception {
		userService.resetPassword(token, resetPasswordRequestDTO);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Password reset successfully. You can now log in.",
						null,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Send magic link", description = "Sends a one-time magic link login email to the user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Magic link sent"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PostMapping("/auth/magic-link/send")
	public ResponseEntity<ApiResponseConfig<Void>> sendMagicLink(
			@Parameter(description = "User email address")
			@Valid @RequestBody MagicLinkTokenRequestDTO magicLinkTokenRequestDTO
	) throws Exception {
		userService.sendMagicLink(magicLinkTokenRequestDTO);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Magic link sent. Please check your email.",
						null,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Verify magic link", description = "Verifies the magic link token and logs the user in")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Magic link verified and user logged in"),
			@ApiResponse(responseCode = "400", description = "Invalid or expired magic link token")
	})
	@GetMapping("/auth/magic-link/verify")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> verifyMagicLink(
			@Parameter(description = "Magic link token from email")
			@RequestParam VerifyMagicLinkTokenDTO verifyMagicLinkTokenDTO
	) throws Exception {
		UserResponseDTO user = userService.verifyMagicLink(verifyMagicLinkTokenDTO);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Magic link verified. Login successful.",
						user,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Resend magic link", description = "Resends a new magic link login email to the user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Magic link resent"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PostMapping("/auth/magic-link/resend")
	public ResponseEntity<ApiResponseConfig<Void>> resendMagicLink(
			@Parameter(description = "User email address")
			@Valid @RequestBody MagicLinkTokenRequestDTO magicLinkTokenRequestDTO
	) throws Exception {
		userService.resendMagicLink(magicLinkTokenRequestDTO);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Magic link resent. Please check your email.",
						null,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Block user", description = "Blocks a user account by ID, revoking all active tokens")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "User blocked successfully"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "400", description = "User is already blocked")
	})
	@PatchMapping("/{userId}/block")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> blockUser(
			@Parameter(description = "User ID")
			@PathVariable String userId
	) throws Exception {
		UserResponseDTO user = userService.blockUser(userId);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"User account blocked successfully.",
						user,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Unblock user", description = "Unblocks a previously blocked user account by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "User unblocked successfully"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "400", description = "User is not blocked")
	})
	@PatchMapping("/{userId}/unblock")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> unblockUser(
			@Parameter(description = "User ID")
			@PathVariable String userId
	) throws Exception {
		UserResponseDTO user = userService.unblockUser(userId);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"User account unblocked successfully.",
						user,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Login via Firebase", description = "Authenticates a user using a Firebase ID token")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Firebase login successful"),
			@ApiResponse(responseCode = "401", description = "Invalid Firebase token")
	})
	@PostMapping("/auth/google-login")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> loginViaFirebase(
			@Parameter(description = "Firebase ID token")
			@RequestParam FirebaseLoginRequestDTO firebaseLoginRequestDTO
	) throws Exception {
		UserResponseDTO user = userService.loginViaFirebase(firebaseLoginRequestDTO);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"Google login successful. Welcome!",
						user,
						HttpStatus.OK.value()
				)
		);
	}
	
	@Operation(summary = "Login via GitHub", description = "Authenticates a user using GitHub OAuth2 and returns tokens")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "GitHub login successful"),
			@ApiResponse(responseCode = "401", description = "Invalid GitHub token")
	})
	@PostMapping("/auth/github-login")
	public ResponseEntity<ApiResponseConfig<UserResponseDTO>> loginViaGithub(
			@Parameter(description = "GitHub user email") @RequestParam String email,
			@Parameter(description = "GitHub full name") @RequestParam(required = false) String name,
			@Parameter(description = "GitHub avatar URL") @RequestParam(required = false) String avatarUrl
	) throws Exception {
		UserResponseDTO user = userService.loginViaGithub(email, name, avatarUrl);
		return ResponseEntity.ok(
				new ApiResponseConfig<>(
						Instant.now(),
						"GitHub login successful. Welcome!",
						user,
						HttpStatus.OK.value()
				)
		);
	}
}