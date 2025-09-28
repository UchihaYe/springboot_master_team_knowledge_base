package com.knowledge.service;

import com.knowledge.dto.request.LoginRequest;
import com.knowledge.dto.request.RegisterRequest;
import com.knowledge.dto.response.LoginResponse;
import com.knowledge.dto.response.UserResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    UserResponse register(RegisterRequest request);

    LoginResponse refreshToken(String refreshToken);

    void logout(String token);

}