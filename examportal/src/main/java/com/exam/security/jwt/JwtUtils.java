package com.exam.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.exam.config.JwtConfig;
import com.exam.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtUtils(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
//here is returen types is secret key which is build in class in security
    private SecretKey getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getJwtSecret());
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Invalid JWT secret configuration", e);
            throw new JwtConfigurationException("Invalid JWT secret", e);
        }
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getJwtExpirationMs()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }
/////////////////////this is when user again send request/////////

    
//   1 Accepts a JWT token as a parameter — this is a string that looks like:
//
//   exple
//    	eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjohn_doeIiwiaWF0IjoxNjg... (etc.)
//    2	Uses the JWT library (Jwts.parser()) to verify and decode the token using the secret signing key.
//
//    3	If the token is valid, it reads the payload (the part with user info).
//
//    	4From the payload, it extracts the subject — which in this case is the username.
//
//    	6 Returns that username as a string.
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}

class JwtConfigurationException extends RuntimeException {
    public JwtConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}