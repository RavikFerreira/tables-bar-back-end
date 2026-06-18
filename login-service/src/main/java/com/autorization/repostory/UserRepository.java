package com.autorization.repostory;

import com.autorization.models.UserRequest;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserRequest, Long> {

    Optional<UserRequest> findByEmail(String email);
}
