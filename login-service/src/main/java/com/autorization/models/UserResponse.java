package com.autorization.models;

import io.micronaut.serde.annotation.Serdeable;

import java.io.Serializable;
@Serdeable
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String jwttoken;

    public UserResponse(String jwttoken){
        this.jwttoken = jwttoken;
    }

    public String getToken(){
        return this.jwttoken;
    }
}
