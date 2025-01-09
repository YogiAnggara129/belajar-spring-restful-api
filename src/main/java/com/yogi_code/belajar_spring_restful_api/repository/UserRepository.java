package com.yogi_code.belajar_spring_restful_api.repository;

import com.yogi_code.belajar_spring_restful_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findFirstByToken(String token);
}
