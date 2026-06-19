package com.autorization.service;

import com.autorization.config.JwtConfig;
import com.autorization.models.UserRequest;
import com.autorization.repostory.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import io.micronaut.security.authentication.Authentication;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;


import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Singleton
public class LoginAPIService {

    private final JwtConfig jwtConfig;

    private final long expiration = 3600; // 1 hora em segundos

    public LoginAPIService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(UserRequest userRequest) {
        return createToken(new HashMap<>(), userRequest.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration * 1000);
        String secret = jwtConfig.getSecret();
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, Authentication authentication) {
        final String username = extractUsername(token);
        return (username.equals(authentication.getName()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        String secret = jwtConfig.getSecret();
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }
}
