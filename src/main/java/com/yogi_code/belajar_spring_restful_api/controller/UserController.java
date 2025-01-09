package com.yogi_code.belajar_spring_restful_api.controller;

import com.yogi_code.belajar_spring_restful_api.entity.User;
import com.yogi_code.belajar_spring_restful_api.model.RegisterUserRequest;
import com.yogi_code.belajar_spring_restful_api.model.UpdateUserRequest;
import com.yogi_code.belajar_spring_restful_api.model.UserResponse;
import com.yogi_code.belajar_spring_restful_api.model.WebResponse;
import com.yogi_code.belajar_spring_restful_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user) {
        final UserResponse response = userService.get(user);
        return WebResponse
                .<UserResponse>builder()
                .data(response)
                .build();
    }

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
        final UserResponse response = userService.update(user, request);
        return WebResponse
                .<UserResponse>builder()
                .data(response)
                .build();
    }
}
