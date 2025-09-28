package com.knowledge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @NotBlank(message = "邮箱或用户名不能为空")
    @Schema(description = "邮箱或用户名", example = "admin@company.com")
    private String emailOrUsername;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "password123")
    private String password;

    @Schema(description = "记住我", example = "false")
    private Boolean rememberMe = false;
}