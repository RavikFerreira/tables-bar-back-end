package com.autorization.service;

import com.autorization.dto.request.AuthenticationRequest;
import com.autorization.dto.request.UserRequest;

import com.autorization.dto.response.AuthenticationResponse;
import com.autorization.models.entity.User;
import com.autorization.models.enums.Role;
import com.autorization.repostory.UserRepository;

import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

@Singleton
@Secured(SecurityRule.IS_ANONYMOUS)
public class AuthenticationService {

    @Inject
    private UserRepository userRepository;
    @Inject
    private PasswordEncode passwordEncode;
    @Inject
    private JwtService jwtService;

    public AuthenticationResponse register (UserRequest userRequest) {
       User user = new User();
       user.setPassword(passwordEncode.encode(userRequest.getPassword()));
       user.setEmail(userRequest.getEmail());
       user.setUserName(userRequest.getUserName());
       user.setRole(Role.USER);

       userRepository.save(user);

        Authentication auth = Authentication.build(
                user.getEmail(),
                List.of(user.getRole().name())
        );

       String token = jwtService.generateToken(auth);
       String refreshToken = jwtService.generateRefresh(new HashMap<>(), auth);
       return new AuthenticationResponse(token, refreshToken);

   }

   public AuthenticationResponse authenticate (AuthenticationRequest authenticationRequest){
       User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow( () -> new IllegalArgumentException("Email ou senha inválidos"));

       if (!BCrypt.checkpw(authenticationRequest.getPassword(), user.getPassword())) {
           throw new IllegalArgumentException("Email ou senha inválidos");
       }
       Authentication auth = Authentication.build(
               user.getEmail(),
               List.of(user.getRole().name())
       );

       String jwtToken = jwtService.generateToken(auth);
       String refreshToken = jwtService.generateRefresh(new HashMap<>(), auth);

       return new AuthenticationResponse(jwtToken, refreshToken);

   }

    public AuthenticationResponse refreshToken (String refreshToken){
        User user = userRepository.findByEmail(jwtService.getEmailFromToken(refreshToken)).orElseThrow( () -> new IllegalArgumentException("Token de atualização inválido"));

        Authentication auth = Authentication.build(
                user.getEmail(),
                List.of(user.getRole().name())
        );

        String jwtToken = jwtService.generateToken(auth);
        String newRefreshToken = jwtService.generateRefresh(new HashMap<>(), auth);

        return new AuthenticationResponse(jwtToken, newRefreshToken);

    }

    public Boolean validateToken(String token){
        return jwtService.validateToken(token);
    }

}
