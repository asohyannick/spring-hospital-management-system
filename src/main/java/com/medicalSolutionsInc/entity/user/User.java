package com.medicalSolutionsInc.entity.user;
import com.medicalSolutionsInc.enumerations.user.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
			
			@Id
			private UUID id;
			
			private String firstName;
			
			private String lastName;
			
			@Indexed(unique = true)
			private String email;
			
			private String password;
			
			@Builder.Default
			private UserRole role = UserRole.DEVOPS_ENGINEER;
			
			@Builder.Default
			private boolean active = true;
			
			@Builder.Default
			private boolean accountBlocked = false;
			
			private String accessToken;
			
			private String refreshToken;
			
			private String avatarUrl;
			
			@Builder.Default
			private Integer failedLoginAttempts = 0;
			
			@Builder.Default
			private Integer twoFactorAttempts = 0;
			
			private String magicLinkToken;
			
			private String magicLinkTokenExpiration;
			
			private Boolean accountVerified = false;
			
			private Boolean accountActive = false;
			
			@CreatedDate
			private Instant createdAt;
			
			@LastModifiedDate
			private Instant updatedAt;
			
}