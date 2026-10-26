package com.autorization.controller;

import com.autorization.dto.request.AuthenticationRequest;
import com.autorization.dto.request.UserRequest;
import com.autorization.dto.response.AuthenticationResponse;
import com.autorization.service.AuthenticationService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/api/v1/auth")
public class AuthenticationController {
    @Inject
    private AuthenticationService authenticationService;

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<AuthenticationResponse> register(@Body UserRequest userRequest) {
        AuthenticationResponse response = authenticationService.register(userRequest);
        return HttpResponse.ok(response);
    }

    @Post("/authenticate")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<AuthenticationResponse> register(@Body AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        return HttpResponse.ok(response);
    }

    @Post("/refresh")
    public HttpResponse<AuthenticationResponse> refresh(@QueryValue("token") String refreshToken) {
        AuthenticationResponse response = authenticationService.refreshToken(refreshToken);
        return HttpResponse.ok(response);
    }

    @Post("/validateToken")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<Boolean> validateToken(@QueryValue("token") String token) {
       Boolean response = authenticationService.validateToken(token);
       return HttpResponse.ok(response);
    }

}
