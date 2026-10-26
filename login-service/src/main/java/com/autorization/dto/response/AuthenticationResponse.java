package com.autorization.dto.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record AuthenticationResponse (
        String accessToken,
        String refreshToken
){
}
