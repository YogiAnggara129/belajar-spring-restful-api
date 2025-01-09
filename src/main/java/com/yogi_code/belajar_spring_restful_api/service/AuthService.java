package com.yogi_code.belajar_spring_restful_api.service;

import com.yogi_code.belajar_spring_restful_api.entity.User;
import com.yogi_code.belajar_spring_restful_api.model.LoginUserRequest;
import com.yogi_code.belajar_spring_restful_api.model.TokenResponse;
import com.yogi_code.belajar_spring_restful_api.repository.UserRepository;
import com.yogi_code.belajar_spring_restful_api.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        final User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next30Days());
        userRepository.save(user);
        return TokenResponse
                .builder()
                .token(user.getToken()).expiredAt(user.getTokenExpiredAt())
                .build();
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000);
    }
}
