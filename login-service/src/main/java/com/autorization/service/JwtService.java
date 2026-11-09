package com.autorization.service;

import com.autorization.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.util.StringUtils;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.SecurityService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class JwtService {

    @Inject
    private SecurityService securityService;
    @Inject
    private JwtConfig jwtConfig;

//    @Value("${jwt.secret}")
//    private String SECRET_KEY;

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken (Authentication authentication){
        return generateToken(new HashMap<>(), authentication);
    }

    public String generateToken (Map<String, Object> extraClaims, Authentication authentication){
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authentication.getRoles())
                .setIssuedAt( new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256).compact();
    }

    public Boolean isTokenValid (String token){

        String username = extractUserName(token);
        if(username == null) return false;

        return !isTokenExpired(token);
    }

    private boolean  isTokenExpired (String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration (String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims (String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSingInKey (){
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateRefresh(Map<String, Object> extraClaims, Authentication authentication){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(authentication.getName())
                .setIssuedAt( new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token){
        return extractUserName(token);
    }

    public Boolean validateToken(String token){
        String userEmail = extractUserName(token);
        if(StringUtils.isNotEmpty(userEmail) && !isTokenExpired(token)){
            return isTokenValid(token);
        }
        return false;
    }




}
