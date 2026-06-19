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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class JwtTokenUtil {

    @Inject
    private UserRepository userRepository;

    private final JwtConfig jwtConfig;
    private final long expiration = 3600; // 1 hora em segundos

    public JwtTokenUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // Extrair o nome de usuário do token JWT
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Obter a data de expiração do token JWT
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extrair informações do token JWT usando uma função
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Para recuperar qualquer informação do token, precisamos da chave secreta
    private Claims getAllClaimsFromToken(String token) {
        String secret = jwtConfig.getSecret();
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    // Verificar se o token expirou
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Gerar um token JWT para um usuário
    public String generateToken(UserRequest userRequest) {
        UserRequest user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getPassword().equals(userRequest.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getEmail());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration * 1000);
        String secret = jwtConfig.getSecret();
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    // Validar o token JWT
    public Boolean validateToken(String token, Authentication authentication) {
        final String username = getUsernameFromToken(token);
        return (username.equals(authentication.getName()) && !isTokenExpired(token));
    }
}
