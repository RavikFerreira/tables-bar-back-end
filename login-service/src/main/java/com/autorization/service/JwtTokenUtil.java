//package com.autorization.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.micronaut.context.annotation.Value;
//import io.micronaut.security.authentication.Authentication;
//import jakarta.inject.Singleton;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Singleton
//public class JwtTokenUtil {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//    // Extrair o nome de usuário do token JWT
//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    // Obter a data de expiração do token JWT
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    // Extrair informações do token JWT usando uma função
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    // Para recuperar qualquer informação do token, precisamos da chave secreta
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
//    // Verificar se o token expirou
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
//    // Gerar um token JWT para um usuário
//    public String generateToken(Authentication authentication) {
//        Map<String, Object> claims = new HashMap<>();
//        return doGenerateToken(claims, authentication.getName());
//    }
//
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    // Validar o token JWT
//    public Boolean validateToken(String token, Authentication authentication) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(authentication.getName()) && !isTokenExpired(token));
//    }
//}
