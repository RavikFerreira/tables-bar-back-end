package com.autorization.controller;

import com.autorization.models.UserRequest;
import com.autorization.models.UserResponse;
//import com.autorization.service.LoginAPIService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/api/auth")
public class LoginController {

//    @Inject
//    private LoginAPIService loginAPIService;

//    @Post("/login")
//    public HttpResponse<UserResponse> createAuthenticationToken(@Body UserRequest authenticationRequest) throws Exception {
//        UserResponse token = loginAPIService.createAuthenticationToken(authenticationRequest);
//        return HttpResponse.created(token);
//    }
    @Get("test")
    public String test() {
        return "OK";
    }
}
