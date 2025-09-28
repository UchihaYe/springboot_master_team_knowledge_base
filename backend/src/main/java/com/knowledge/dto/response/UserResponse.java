package com.knowledge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户响应")
public class UserResponse {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "显示名称")
    private String displayName;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "是否系统管理员")
    private Boolean isSystemAdmin;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginAt;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}