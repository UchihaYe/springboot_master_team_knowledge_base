package com.knowledge.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.knowledge.common.exception.BusinessException;
import com.knowledge.common.result.ResultCode;
import com.knowledge.common.util.JwtUtil;
import com.knowledge.common.util.RedisUtil;
import com.knowledge.common.util.SecurityUtil;
import com.knowledge.dto.request.LoginRequest;
import com.knowledge.dto.request.RegisterRequest;
import com.knowledge.dto.response.LoginResponse;
import com.knowledge.dto.response.UserResponse;
import com.knowledge.entity.LoginAttempt;
import com.knowledge.entity.User;
import com.knowledge.mapper.UserMapper;
import com.knowledge.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${app.security.max-login-attempts}")
    private Integer maxLoginAttempts;

    @Value("${app.security.lock-duration}")
    private Integer lockDuration;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        String key = "login_attempts:" + request.getEmailOrUsername();
        Integer attempts = (Integer) redisUtil.get(key);
        if (attempts != null && attempts >= maxLoginAttempts) {
            throw new BusinessException(ResultCode.LOGIN_ATTEMPTS_EXCEEDED);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmailOrUsername(),
                            request.getPassword()
                    )
            );

            User user = userMapper.findByEmail(request.getEmailOrUsername());
            if (user == null) {
                throw new BusinessException(ResultCode.USER_NOT_FOUND);
            }

            if (!"ACTIVE".equals(user.getStatus())) {
                throw new BusinessException(ResultCode.ACCOUNT_LOCKED);
            }

            user.setLastLoginAt(LocalDateTime.now());
            userMapper.updateById(user);

            redisUtil.delete(key);

            String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail());

            redisUtil.set("refresh_token:" + user.getId(), refreshToken, refreshExpiration, TimeUnit.SECONDS);

            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);

            return new LoginResponse(accessToken, refreshToken, jwtExpiration, userResponse);

        } catch (Exception e) {
            if (attempts == null) {
                attempts = 0;
            }
            attempts++;
            redisUtil.set(key, attempts, lockDuration, TimeUnit.SECONDS);

            recordLoginAttempt(request.getEmailOrUsername(), false, e.getMessage());

            if (e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException(ResultCode.INVALID_CREDENTIALS);
        }
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "密码和确认密码不匹配");
        }

        if (userMapper.findByEmail(request.getEmail()) != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "邮箱已被注册");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(SecurityUtil.encodePassword(request.getPassword()));
        user.setDisplayName(request.getFullName());
        user.setIsSystemAdmin(false);
        user.setStatus("ACTIVE");

        userMapper.insert(user);

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        if (StrUtil.isBlank(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        }

        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String storedToken = (String) redisUtil.get("refresh_token:" + userId);

        if (!refreshToken.equals(storedToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail());

        redisUtil.set("refresh_token:" + userId, newRefreshToken, refreshExpiration, TimeUnit.SECONDS);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);

        return new LoginResponse(newAccessToken, newRefreshToken, jwtExpiration, userResponse);
    }

    @Override
    public void logout(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId != null) {
            redisUtil.delete("refresh_token:" + userId);
        }
    }


    private void recordLoginAttempt(String email, boolean successful, String failureReason) {
        try {
            LoginAttempt attempt = new LoginAttempt();
            attempt.setEmail(email);
            attempt.setSuccessful(successful);
            attempt.setFailureReason(failureReason);
            attempt.setIpAddress("127.0.0.1");
            attempt.setUserAgent("Unknown");

        } catch (Exception e) {
            log.error("记录登录尝试失败", e);
        }
    }
}