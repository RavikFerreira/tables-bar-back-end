//package com.autorization.service;
//
//import com.autorization.models.UserRequest;
//import com.autorization.models.UserResponse;
//import com.autorization.repostory.UserRepository;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.micronaut.context.annotation.Value;
//import io.micronaut.security.authentication.Authentication;
//import io.micronaut.security.authentication.AuthenticationResponse;
//import io.micronaut.security.authentication.Authenticator;
//import io.micronaut.security.authentication.UsernamePasswordCredentials;
//import jakarta.inject.Inject;
//import jakarta.inject.Singleton;
//import reactor.core.publisher.Mono;
//
//import java.util.*;
//import java.util.function.Function;
//
//@Singleton
//public class LoginAPIService {

//    @Inject
//    private Authenticator authenticator;

//    @Inject
//    private LoginAPIService jwtTokenUtil;
//
//    @Inject
//    private UserRepository userRepository;

//    @Inject
//    private Authentication authentication;

//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;

//    public String generateToken(Authentication authentication) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, authentication.getName());
//    }
//
//    private String createToken(Map<String, Object> claims, String subject) {
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + expiration * 1000);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    public boolean validateToken(String token, Authentication authentication) {
//        final String username = extractUsername(token);
//        return (username.equals(authentication.getName()) && !isTokenExpired(token));
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
//    private boolean isTokenExpired(String token) {
//        final Date expiration = extractExpiration(token);
//        return expiration.before(new Date());
//    }
//
//
//    public UserResponse createAuthenticationToken(UserRequest authenticationRequest) throws Exception {
//        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
//        authentication = loadUserByEmail(authenticationRequest.getEmail());
//        final String token = jwtTokenUtil.generateToken(authentication);
//        return new UserResponse(token);
//    }
//
//    private Authentication authenticate(String email, String password) throws Exception {
//        AuthenticationResponse response = (AuthenticationResponse) Mono.from(
//                authenticator.authenticate(
//                        null,
//                        new UsernamePasswordCredentials(email, password)
//                )
//        ).block();
//
//        if (response instanceof Authentication authentication) {
//            return authentication;
//        }
//        throw new Exception("Email ou senha inválidos");
//    }
//
//    public Authentication loadUserByEmail(String email) {
//        UserRequest user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//        return Authentication.build(
//                user.getEmail(),
//                List.of(user.getRoles().toString()));
//    }
//}
