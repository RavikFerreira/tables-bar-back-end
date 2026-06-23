package com.autorization.models.entity;

import com.autorization.models.enums.Role;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Serdeable
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(Long id, Role role, String password, String email, String userName) {
        this.id = id;
        this.role = role;
        this.password = password;
        this.email = email;
        this.userName = userName;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
