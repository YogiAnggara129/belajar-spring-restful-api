package com.yogi_code.belajar_spring_restful_api.service;

import com.yogi_code.belajar_spring_restful_api.entity.User;
import com.yogi_code.belajar_spring_restful_api.model.RegisterUserRequest;
import com.yogi_code.belajar_spring_restful_api.model.UpdateUserRequest;
import com.yogi_code.belajar_spring_restful_api.model.UserResponse;
import com.yogi_code.belajar_spring_restful_api.repository.UserRepository;
import com.yogi_code.belajar_spring_restful_api.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        final User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        return new UserResponse(user.getUsername(), user.getName());
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        if (!Optional.ofNullable(request.getName()).orElse("").isEmpty()) {
            user.setName(request.getName());
        }
        if (!Optional.ofNullable(request.getPassword()).orElse("").isEmpty()) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);

        return new UserResponse(user.getUsername(), user.getName());
    }
}
