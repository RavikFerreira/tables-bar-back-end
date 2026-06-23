package com.autorization.config;


import com.autorization.service.JwtService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.List;

@Singleton
@Filter("/**")
public class JwtAuthenticationFilter implements HttpServerFilter {
    @Inject
    private JwtService jwtService;

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        String authorization = request.getHeaders().getAuthorization().orElse(null);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return chain.proceed(request);
        }

        String token = authorization.substring(7);

        try {
            if (jwtService.isTokenValid(token)) {

                String email = jwtService.extractUserName(token);

                Authentication authentication = Authentication.build(
                        email,
                        List.of()
                );

                request.setAttribute(Authentication.class.getName(), authentication);
            }

        } catch (Exception e) {
            // token inválido → apenas não autentica
        }

        return chain.proceed(request);
    }
}
