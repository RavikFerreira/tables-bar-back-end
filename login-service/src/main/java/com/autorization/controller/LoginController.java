package com.autorization.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

@Controller("/api/auth")
public class LoginController {

    @Get("/admin")
    @Secured("ADMIN")
    public HttpResponse<String>  testAdmin() {
        return HttpResponse.ok("OK, ADMIN");
    }

    @Get("/user")
    @Secured({"USER", "ADMIN"})
    public HttpResponse<String>  testUser() {
        return HttpResponse.ok("OK, USER");
    }
}
