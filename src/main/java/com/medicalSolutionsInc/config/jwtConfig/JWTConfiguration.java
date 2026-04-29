package com.medicalSolutionsInc.config.jwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JWTConfiguration {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

	public String generateAccessToken(String email, String role) {
		Instant now = Instant.now();
		return Jwts.builder()
				       .subject(email)
				       .claims(Map.of(
						       "role", role,
						       "type", "access"
				       ))
				       .issuedAt(Date.from(now))
				       .expiration(Date.from(now.plusMillis(accessTokenExpirationMs)))
				       .signWith(getSecretKey())
				       .compact();
	}

	public String generateRefreshToken(String email) {
		Instant now = Instant.now();
		return Jwts.builder()
				       .subject(email)
				       .claims(Map.of("type", "refresh"))
				       .issuedAt(Date.from(now))
				       .expiration(Date.from(now.plusMillis(refreshTokenExpirationMs)))
				       .signWith(getSecretKey())
				       .compact();
	}
	

	public String generateAccountSetupToken(String email) {
		Instant now = Instant.now();
		return Jwts.builder()
				       .subject(email)
				       .claims(Map.of("type", "account_setup"))
				       .issuedAt(Date.from(now))
				       .expiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
				       .signWith(getSecretKey())
				       .compact();
	}
	
	public String generatePasswordResetToken(String email) {
		Instant now = Instant.now();
		return Jwts.builder()
				       .subject(email)
				       .claims(Map.of("type", "password_reset"))
				       .issuedAt(Date.from(now))
				       .expiration(Date.from(now.plus ( 5, ChronoUnit.MINUTES )))
				       .signWith(getSecretKey())
				       .compact();
	}
	
	public String generateMagicLinkToken(String email) {
		Instant now = Instant.now();
		return Jwts.builder()
				       .subject(email)
				       .claims( Map.of("type", "magic_link"))
				       .issuedAt(Date.from(now))
				       .expiration(Date.from(now.plus ( 5, ChronoUnit.MINUTES )))
				       .signWith(getSecretKey())
				       .compact();
	}

    public Claims validateToken(String token) {
        return io.jsonwebtoken.Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

	public boolean isTokenValid(String token) {
		try {
			validateToken(token);
			return true;
		} catch ( ExpiredJwtException e) {
			log.warn("JWT token has expired: {}", e.getMessage());
		} catch ( MalformedJwtException e) {
			log.warn("JWT token is malformed: {}", e.getMessage());
		} catch (SignatureException e) {
			log.warn("JWT signature is invalid: {}", e.getMessage());
		} catch ( UnsupportedJwtException e) {
			log.warn("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.warn("JWT token is empty or null: {}", e.getMessage());
		}
		return false;
	}

	public boolean isTokenExpired(String token) {
		try {
			return validateToken(token).getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

	public String extractEmail(String token) {
		return validateToken(token).getSubject();
	}
	
	public String extractRole(String token) {
		return validateToken(token).get("role", String.class);
	}
	
	public String extractTokenType(String token) {
		return validateToken(token).get("type", String.class);
	}
	
	public Date extractExpiration(String token) {
		return validateToken(token).getExpiration();
	}
	
	public boolean isAccessToken(String token) {
		return "access".equals(extractTokenType(token));
	}
	
	public boolean isRefreshToken(String token) {
		return "refresh".equals(extractTokenType(token));
	}
	
	public boolean isPasswordResetToken(String token) {
		return "password_reset".equals(extractTokenType(token));
	}
	
	public boolean isAccountSetupToken(String token) {
		return "account_setup".equals(extractTokenType(token));
	}
	
	public boolean isMagicLinkToken(String token) {
		return "magic_link".equals(extractTokenType(token));
	}
}
