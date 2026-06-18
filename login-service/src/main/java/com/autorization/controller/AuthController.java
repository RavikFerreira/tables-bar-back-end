//package com.autorization.controller;
//
//import com.autorization.models.UserRequest;
//import com.autorization.service.JwtTokenUtil;
//import io.micronaut.http.HttpResponse;
//import io.micronaut.http.annotation.Body;
//import io.micronaut.http.annotation.Controller;
//import io.micronaut.http.annotation.Post;
//import io.micronaut.security.authentication.*;
//import jakarta.inject.Inject;
//import reactor.core.publisher.Mono;
//
//@Controller
//public class AuthController {
//    @Inject
//    private Authenticator authenticator;
//
//    @Post("/login")
//    public HttpResponse<?> createAuthenticationToken(@Body UserRequest authRequest) {
//        AuthenticationRequest<String, String> request =
//                new UsernamePasswordCredentials(
//                        authRequest.getEmail(),
//                        authRequest.getPassword()
//                );
//        AuthenticationResponse response = (AuthenticationResponse) Mono.from(
//                authenticator.authenticate(null, request)).block();
//
//        assert response != null;
//        if (response.isAuthenticated()) {
//                return HttpResponse.ok(response);
//            }
//
//            return HttpResponse.unauthorized();
//        }
//}
