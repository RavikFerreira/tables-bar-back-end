package com.autorization.service;

import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;

@Singleton
public class PasswordEncode {

    public String encode(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matches (String rawPassowrd, String hash){
        return BCrypt.checkpw(rawPassowrd, hash);
    }
}
