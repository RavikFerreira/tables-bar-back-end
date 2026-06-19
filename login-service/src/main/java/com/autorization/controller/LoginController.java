package com.autorization.controller;

import com.autorization.models.UserRequest;
import com.autorization.models.UserResponse;
import com.autorization.repostory.UserRepository;
import com.autorization.service.JwtTokenUtil;
import com.autorization.service.LoginAPIService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Controller("/api/auth")
public class LoginController {

    @Inject
    private LoginAPIService loginAPIService;

    @Post("/login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<UserResponse> createAuthenticationToken(@Body UserRequest authenticationRequest){
        String token = loginAPIService.generateToken(authenticationRequest);
        return HttpResponse.ok(new UserResponse(token));
    }

    @Get("test")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public String test() {
        return "OK";
    }

//    private void authenticate(String email, String password) throws Exception {
//        AuthenticationResponse response = (AuthenticationResponse) Mono.from(
//                authenticator.authenticate(
//                        null,
//                        new UsernamePasswordCredentials(email, password)
//                )
//        ).block();
//
//        if (response instanceof Authentication authentication) {
//            return;
//        }
//        throw new Exception("Email ou senha inválidos");
//    }
}
