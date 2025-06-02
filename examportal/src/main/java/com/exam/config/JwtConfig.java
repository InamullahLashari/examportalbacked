package com.exam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;



@Configuration
public class JwtConfig {
	
	 @Value("${app.jwt.secret}") 
    private String jwtSecret;
  //The @Value annotations in your JwtConfig class are reading 
  	//values from the application.properties file you've shown. 
    @Value("${app.jwt.expirationMs}")
    private int jwtExpirationMs;
    
    @Value("${app.jwt.refreshExpirationMs}")
    private long refreshTokenDurationMs;

    @PostConstruct
    public void validate() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret cannot be null or empty");
        }
        
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            if (keyBytes.length < 32) {
                throw new IllegalStateException(
                    "JWT secret must be at least 256 bits (32 bytes) after Base64 decoding. " +
                    "Current size: " + (keyBytes.length * 8) + " bits. " +
                    "Generate a new key with: openssl rand -base64 32"
                );
            }
        } catch (Exception e) {
            throw new IllegalStateException("JWT secret must be a valid Base64-encoded string", e);
        }
        
        if (jwtExpirationMs <= 0) {
            throw new IllegalStateException("JWT expiration time must be positive");
        }
        
        if (refreshTokenDurationMs <= 0) {
            throw new IllegalStateException("Refresh token duration must be positive");
        }
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public long getRefreshTokenDurationMs() {
        return refreshTokenDurationMs;
    }
}



