//package com.autorization.config;
//
//import io.micronaut.http.HttpRequest;
//import io.micronaut.security.authentication.AuthenticationRequest;
//import io.micronaut.security.authentication.AuthenticationResponse;
//import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
//import jakarta.inject.Singleton;
//
//
//import java.util.List;
//
//@Singleton
//public class AuthenticationProviderUserPassword implements HttpRequestAuthenticationProvider<Object> {
//
//    @Override
//    public AuthenticationResponse authenticate(
//            HttpRequest<Object> requestContext,
//            AuthenticationRequest<String, String> authenticationRequest) {
//
//        String email = authenticationRequest.getIdentity();
//        String password = authenticationRequest.getSecret();
//
//        if ("admin@email.com".equals(email) && "123456".equals(password)) {
//            return AuthenticationResponse.success(
//                    email,
//                    List.of("ROLE_USER")
//            );
//        }
//
//        return AuthenticationResponse.failure();
//    }
//}